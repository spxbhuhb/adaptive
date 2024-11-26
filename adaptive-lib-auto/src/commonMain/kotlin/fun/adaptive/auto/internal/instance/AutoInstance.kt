package `fun`.adaptive.auto.internal.instance

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.api.diff
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.api.AutoCollectionListener
import `fun`.adaptive.auto.api.AutoGeneric
import `fun`.adaptive.auto.api.AutoItemListener
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.backend.AutoCollectionBackend
import `fun`.adaptive.auto.internal.backend.AutoItemBackend
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.internal.persistence.AutoPersistence
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.PEER_ID_CONNECTING
import `fun`.adaptive.auto.model.PeerId
import `fun`.adaptive.auto.model.operation.AutoAdd
import `fun`.adaptive.auto.model.operation.AutoEmpty
import `fun`.adaptive.auto.model.operation.AutoOperation
import `fun`.adaptive.auto.model.operation.AutoRemove
import `fun`.adaptive.auto.model.operation.AutoSyncBatch
import `fun`.adaptive.auto.model.operation.AutoSyncEnd
import `fun`.adaptive.auto.model.operation.AutoUpdate
import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.utility.RequireLock
import `fun`.adaptive.utility.ThreadSafe
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.safeCall
import `fun`.adaptive.utility.safeSuspendCall
import `fun`.adaptive.utility.use
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The base class for all auto instances.
 */
abstract class AutoInstance<BE : AutoBackend<IT>, PT : AutoPersistence<VT, IT>, VT, IT : AdatClass>(
    val origin: Boolean,
    val defaultWireFormat: AdatClassWireFormat<*>?,
    val wireFormatProvider: WireFormatProvider,
    val scope: CoroutineScope
) {
    private val lock = getLock()

    @ThreadSafe
    val handle: AutoHandle
        get() = lock.use { checkNotNull(unsafeHandle) }

    @RequireLock
    private var unsafeHandle: AutoHandle? = null

    @ThreadSafe
    val time: LamportTimestamp
        get() = lock.use { unsafeTime }

    @RequireLock
    private var unsafeTime = LamportTimestamp(unsafeHandle?.peerId ?: PEER_ID_CONNECTING, 0)

    @RequireLock
    internal var logger: AdaptiveLogger = getLogger("auto.CONNECTING   ")

    @RequireLock
    internal var connectionInfo: AutoConnectionInfo<VT>? = null

    @RequireLock
    val thisItemId : ItemId?
        get() = connectionInfo?.connectingHandle?.itemId

    @ThreadSafe
    val remoteHandle : AutoHandle
        get() = lock.use { connectionInfo!!.acceptingHandle }

    internal lateinit var backend: BE

    internal lateinit var persistence: PT

    private var worker : AutoWorker? = null

    private var closePeers = emptyList<PeerId>()

    private var connectors = listOf<AutoConnector>()

    private var itemListeners = emptyList<AutoItemListener<IT>>()

    private var collectionListeners = emptyList<AutoCollectionListener<IT>>()

    internal val store = AutoAdatStore(this)

    @RequireLock
    private val isInitialized: Boolean
        get() = ((time >= (connectionInfo?.acceptingTime ?: LamportTimestamp.ORIGIN)))

    // --------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------

    @ThreadSafe
    internal fun setInfo(connectionInfo: AutoConnectionInfo<VT>, worker: AutoWorker?, trace: Boolean) {
        lock.use {
            this.connectionInfo = connectionInfo

            connectionInfo.connectingHandle.also {
                unsafeHandle = it

                if (connectionInfo.connectionType == AutoConnectionType.Origin) {
                    unsafeTime = LamportTimestamp.ORIGIN
                } else {
                    unsafeTime = LamportTimestamp(it.peerId, 0)
                }

                if (it.itemId != null) {
                    (backend as? AutoItemBackend<*>)?.also { be -> be.itemId = it.itemId }
                }

                // ${it.itemId?.let { ".$it" } ?: ""}
                val loggerName = "auto.${it.globalId.toShort()}.${it.peerId.toString().padStart(6, '0')}"
                logger = getLogger(loggerName)
                if (trace) logger.enableFine()
            }
        }

        if (connectionInfo.connectionType == AutoConnectionType.Service || (origin && worker != null)) {
            checkNotNull(worker) { "cannot register auto instance without a worker ($this)" }
            this.worker = worker
            worker.register(this)
            trace { "REGISTERED  :: $worker" }
        }

        trace { "APPLIED  :: $connectionInfo" }
    }

    @ThreadSafe
    fun stop() {
        lock.use {
            worker?.deregister(this)

            val scope = CoroutineScope(Dispatchers.Default)

            scope.launch { connectors.forEach { safeSuspendCall(logger) { it.disconnect() } } }
            connectors = emptyList()

            itemListeners.forEach { safeCall(logger) { it.onStop() } }
            itemListeners = emptyList()

            collectionListeners.forEach { safeCall(logger) { it.onStop() } }
            collectionListeners = emptyList()
        }

        logger.fine("STOP")
    }

    // --------------------------------------------------------------------------------
    // Operations initiated locally (through this instance)
    // --------------------------------------------------------------------------------

    @ThreadSafe
    internal fun localAdd(item: IT) {
        lock.use {
            val (operation, added) = backend.localAdd(nextTime(), item)

            onLocalChange(operation.itemId, added, null)
            distribute(operation)

            trace("localAdd") { "APPLIED  :: opItemId=${operation.itemId} added=$added" }
        }
    }

    @ThreadSafe
    internal fun localUpdate(itemId: ItemId?, updates: Collection<Pair<String, Any?>>) {
        lock.use {
            val safeItemId = itemId ?: thisItemId
            checkNotNull(safeItemId) { "no item id passed and connection info does not contain one in $this" }

            val original = backend.getItem(safeItemId)
            checkNotNull(original)

            val (operation, updated) = backend.localUpdate(nextTime(), safeItemId, updates)

            persistenceUpdate(safeItemId, updated)
            onLocalChange(operation.itemId, updated, original)
            distribute(operation)

            trace("localUpdate") { "APPLIED  :: opItemId=${operation.itemId} diff=${updated.diff(original).map { it.path to updated.getValue(it.path) }}" }
        }
    }

    @ThreadSafe
    internal fun localRemove(itemId: ItemId) {
        lock.use {
            val (operation, removed) = backend.localRemove(nextTime(), itemId)

            removed?.let { onLocalRemove(itemId, it) }
            distribute(operation)

            trace("localRemove") { "APPLIED  :: itemId=${itemId} removed=$removed" }
        }
    }

    // --------------------------------------------------------------------------------
    // Operations from peers (received through a connector)
    // --------------------------------------------------------------------------------

    @ThreadSafe
    internal fun remoteReceive(operation: AutoOperation) {
        lock.use {
            operation.apply(this)
        }
    }

    @RequireLock
    internal open fun remoteAdd(operation: AutoAdd) {
        val (timestamp, added) = backend.remoteAdd(operation)

        if (timestamp != null) {
            receive(timestamp)
            onRemoteChange(operation.itemId, added, null)
            distribute(operation)

            trace { "APPLIED  :: ${operation.itemId} $added" }
        } else {
            trace { "SKIPPED  :: ${operation.itemId}" }
        }
    }

    @RequireLock
    internal fun remoteUpdate(operation: AutoUpdate) {
        val (timestamp, original, updated) = backend.remoteUpdate(operation)

        if (timestamp != null) {
            receive(timestamp)
            persistenceUpdate(operation.itemId, updated)
            onRemoteChange(operation.itemId, updated, original)
            distribute(operation)

            trace { "APPLIED  :: ${operation.itemId} $updated $original" }
        } else {
            trace { "SKIPPED  :: ${operation.itemId}" }
        }
    }

    @RequireLock
    internal open fun remoteRemove(operation: AutoRemove) {
        val (timestamp, removed) = backend.remove(operation)

        if (timestamp != null) {
            receive(timestamp)
            removed.forEach {
                onRemoteRemove(it.first, it.second)
            }
            distribute(operation)
            trace { "APPLIED  :: ${operation.itemIds} $removed" }
        } else {
            trace { "SKIPPED  :: ${operation.itemIds}" }
        }
    }

    @RequireLock
    internal fun remoteEmpty(operation: AutoEmpty) {
        receive(operation.timestamp)
        trace { "APPLIED  :: opTime=${operation.timestamp}" }
        onInit(emptyList())
        onSyncEnd()
    }

    @RequireLock
    internal open fun remoteSyncBatch(operation: AutoSyncBatch) {
        operation.additions.forEach { it.apply(this) }
        operation.updates.forEach { it.apply(this) }
    }

    @RequireLock
    internal open fun remoteSyncEnd(operation: AutoSyncEnd) {
        backend.syncEnd(operation)
        receive(operation.timestamp)
        trace { "APPLIED  :: opTime=${operation.timestamp}" }
        onSyncEnd()
    }

    @RequireLock
    private fun distribute(operation: AutoOperation) {
        val connectors = connectors
        val closePeers = closePeers
        val thisPeer = unsafeHandle !!.peerId

        for (connector in connectors) {
            val peerHandle = connector.peerHandle
            val sourcePeer = operation.timestamp.peerId

            // do not send operations back to the peer that created them
            if (peerHandle.peerId == sourcePeer) {
                // println("BackendBase.SKIP: this=${context.handle} peer=${peerHandle.peerId} item=${peerHandle.itemId} operation.timestamp=${operation.timestamp}")
                continue
            }

            // do not send list and non-item operations to single-item peers
            if (peerHandle.itemId != null) {
                if (operation !is AutoUpdate) {
                    // println("BackendBase.SKIP: this=${context.handle} peer=${peerHandle.peerId} item=${peerHandle.itemId} operation.timestamp=${operation.timestamp}")
                    continue
                }
                if (operation.itemId != peerHandle.itemId) {
                    // println("BackendBase.SKIP: this=${context.handle} peer=${peerHandle.peerId} item=${peerHandle.itemId} operation.timestamp=${operation.timestamp}")
                    continue
                }
            }

            // do not send operations originated from peers far away
            // FIXME operation send for far peers
            if (sourcePeer != thisPeer && sourcePeer !in closePeers) {
                // println("BackendBase.SKIP: this=${context.handle} peer=${peerHandle.peerId} item=${peerHandle.itemId} operation.timestamp=${operation.timestamp}")
                continue
            }

            // println("BackendBase.SEND: this=${context.handle} peer=${peerHandle.peerId} item=${peerHandle.itemId} operation.timestamp=${operation.timestamp}")
            connector.send(operation)
        }
    }

    // --------------------------------------------------------------------------------
    // Functions to get connection info from this node
    // --------------------------------------------------------------------------------

    @ThreadSafe
    internal fun connectInfo(type: AutoConnectionType = AutoConnectionType.Service): AutoConnectionInfo<VT> =
        lock.use {
            // Using the item id from connection info here works for items and collections as well.
            // For collections this will pass null, for items it will pass the actual value.
            makeConnectInfo<VT>(thisItemId, type)
        }

    @ThreadSafe
    internal fun <IT> connectInfo(itemId: ItemId, type: AutoConnectionType = AutoConnectionType.Service): AutoConnectionInfo<IT> =
        lock.use { makeConnectInfo<IT>(itemId, type) }

    @ThreadSafe
    internal fun <IT> connectInfo(type: AutoConnectionType = AutoConnectionType.Service, filterFun: (IT) -> Boolean): AutoConnectionInfo<IT>? {
        val safeBackend = backend
        check(safeBackend is AutoCollectionBackend<*>) { "connecting with filter function is supported only by AutoCollectionBackend" }

        @Suppress("UNCHECKED_CAST")
        safeBackend as AutoCollectionBackend<IT>

        return lock.use {
            val itemBackend = safeBackend.firstOrNull(filterFun) ?: return null
            makeConnectInfo<IT>(itemBackend.itemId, type)
        }
    }

    @RequireLock
    private fun <T> makeConnectInfo(itemId: ItemId?, type: AutoConnectionType = AutoConnectionType.Service): AutoConnectionInfo<T> {
        val connectTime = nextTime()
        return AutoConnectionInfo<T>(
            type,
            handle,
            connectTime,
            AutoHandle(handle.globalId, connectTime.timestamp, itemId)
        )
    }

    // --------------------------------------------------------------------------------
    // Peer synchronization
    // --------------------------------------------------------------------------------

    internal suspend fun syncPeer(connector: AutoConnector, connectingTime: LamportTimestamp) {
        backend.syncPeer(connector, connectingTime, null)
    }

    // --------------------------------------------------------------------------------
    // Time
    // --------------------------------------------------------------------------------

    @RequireLock
    protected fun receive(receivedTime: LamportTimestamp) {
        unsafeTime = unsafeTime.receive(receivedTime)
    }

    @RequireLock
    protected fun nextTime(): LamportTimestamp =
        unsafeTime.increment().also { unsafeTime = it }

    // --------------------------------------------------------------------------------
    // Persistence
    // --------------------------------------------------------------------------------

    /**
     * Called when an origin instance is initialized the first time. In that case
     * the initial value has to be persisted.
     */
    abstract fun persistenceInit()

    /**
     * Called on local and remote item updates.
     */
    abstract fun persistenceUpdate(itemId : ItemId, value : IT)

    // --------------------------------------------------------------------------------
    // Peers
    // --------------------------------------------------------------------------------

    /**
     * Add a connection to a peer. The instance launches `AutoBackend.syncPeer` to send all known
     * operations that happened after the `connector.peerTime` to the peer.
     */
    @ThreadSafe
    fun addConnector(connector: AutoConnector): LamportTimestamp {

        lock.use {
            connectors += connector
            closePeers += connector.peerHandle.peerId
        }

        return time
    }

    @ThreadSafe
    fun removeConnector(handle: AutoHandle) {
        lock.use {
            val toRemove = connectors.filter { it.peerHandle.peerId == handle.peerId }

            connectors -= toRemove
            closePeers -= handle.peerId

            toRemove.forEach {
                logger.fine { "remove connector $handle" }
                it.dispose()
            }
        }
    }

    // --------------------------------------------------------------------------------
    // Listeners
    // --------------------------------------------------------------------------------

    @ThreadSafe
    fun addListener(listener: AutoItemListener<IT>) {
        lock.use {
            itemListeners += listener
            if (isInitialized) listener.onInit(thisItemId!!, getItem())
        }
    }

    @ThreadSafe
    fun removeListener(listener: AutoItemListener<IT>) {
        lock.use {
            itemListeners -= listener
        }
    }

    @ThreadSafe
    fun addListener(listener: AutoCollectionListener<IT>) {
        lock.use {
            collectionListeners += listener
            if (isInitialized) listener.onInit(getItems())
        }
    }

    // --------------------------------------------------------------------------------
    // Listener callbacks
    // --------------------------------------------------------------------------------

    @RequireLock
    private fun onLocalChange(itemId: ItemId, newValue: IT, oldValue: IT?) {
        for (listener in itemListeners) {
            listener.onLocalChange(itemId, newValue, oldValue)
        }
        for (listener in collectionListeners) {
            listener.onLocalChange(itemId, newValue, oldValue)
        }
    }

    @RequireLock
    private fun onLocalRemove(itemId: ItemId, value: IT) {
        for (listener in itemListeners) {
            listener.onLocalRemove(itemId, value)
        }
        for (listener in collectionListeners) {
            listener.onLocalRemove(itemId, value)
        }
    }

    @RequireLock
    private fun onInit(values: Collection<IT>) {
        for (listener in collectionListeners) {
            listener.onInit(values)
        }
    }

    @RequireLock
    private fun onRemoteChange(itemId: ItemId, newValue: IT, oldValue: IT?) {
        for (listener in itemListeners) {
            listener.onRemoteChange(itemId, newValue, oldValue)
        }
        for (listener in collectionListeners) {
            listener.onRemoteChange(itemId, newValue, oldValue)
        }
    }

    @RequireLock
    private fun onRemoteRemove(itemId: ItemId, value: IT) {
        for (listener in itemListeners) {
            listener.onLocalRemove(itemId, value)
        }
        for (listener in collectionListeners) {
            listener.onLocalRemove(itemId, value)
        }
    }

    @RequireLock
    private fun onSyncEnd() {
        for (listener in collectionListeners) {
            listener.onSyncEnd()
        }
    }

    // --------------------------------------------------------------------------------
    // Access, utility
    // --------------------------------------------------------------------------------

    @ThreadSafe
    @Suppress("UNCHECKED_CAST")
    protected fun getItem(): IT =
        lock.use {
            (backend as AutoItemBackend<IT>).getItem()
        }

    @ThreadSafe
    @Suppress("UNCHECKED_CAST")
    protected fun getItemOrNull(): IT? =
        lock.use {
            (backend as AutoItemBackend<IT>).getItemOrNull()
        }

    @ThreadSafe
    @Suppress("UNCHECKED_CAST")
    protected fun getItems(): Collection<IT> =
        lock.use {
            (backend as AutoCollectionBackend<IT>).getItems()
        }

    @ThreadSafe
    fun itemId(item: IT) =
        checkNotNull(item.adatContext?.id as? ItemId) { "this item is not managed by an auto instance" }

    @ThreadSafe
    fun globalId(): UUID<AutoGeneric> =
        lock.use {
            handle.globalId
        }

    // --------------------------------------------------------------------------------
    // Logging
    // --------------------------------------------------------------------------------

    internal fun error(message: String, e: Exception) {
        logger.error(message, e)
    }

    @CallSiteName
    open fun trace(callSiteName: String = "", builder: () -> String) {
        logger.fine { "[ ${callSiteName.substringAfterLast('.').padEnd(15, ' ')} @ $time] ${builder()}" }
    }

}