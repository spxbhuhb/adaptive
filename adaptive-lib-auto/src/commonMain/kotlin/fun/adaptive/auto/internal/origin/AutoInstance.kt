package `fun`.adaptive.auto.internal.origin

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
import `fun`.adaptive.auto.internal.connector.DirectConnector
import `fun`.adaptive.auto.internal.persistence.AutoPersistence
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
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
import `fun`.adaptive.utility.safeSuspendCall
import `fun`.adaptive.utility.use
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration

/**
 * The base class for all auto instances.
 */
abstract class AutoInstance<BE : AutoBackend<IT>, PT : AutoPersistence<VT, IT>, VT, IT : AdatClass>(
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
    private var unsafeTime = LamportTimestamp(unsafeHandle?.peerId ?: PeerId.CONNECTING, 0)

    internal var logger: AdaptiveLogger = getLogger("auto.CONNECTING")

    private lateinit var connectionInfo: AutoConnectionInfo<VT>

    internal lateinit var backend: BE

    internal lateinit var persistence: PT

    private var closePeers = emptyList<PeerId>()

    private var connectors = listOf<AutoConnector>()

    private var itemListeners = emptyList<AutoItemListener<IT>>()

    private var collectionListeners = emptyList<AutoCollectionListener<IT>>()

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
                }

                // ${it.itemId?.let { ".$it" } ?: ""}
                val loggerName = "auto.${it.globalId.toShort()}.${it.peerId}"
                logger = getLogger(loggerName)
                if (trace) logger.enableFine()
            }
        }

        if (connectionInfo.connectionType == AutoConnectionType.Service) {
            checkNotNull(worker) { "cannot register auto instance without a worker ($this)" }
            worker.register(this)
        }

        trace("SET-INFO") { "$connectionInfo $worker" }
    }

    @ThreadSafe
    suspend fun stop() {
        lock.use { connectors }.forEach { safeSuspendCall(logger) { it.disconnect() } }
        logger.fine("backend context stopped")
    }

    suspend fun waitForSync(connectInfo: AutoConnectionInfo<VT>, timeout: Duration) {
        backend.waitForSync(connectInfo, timeout)
    }

    // --------------------------------------------------------------------------------
    // Operations initiated locally (through this instance)
    // --------------------------------------------------------------------------------

    @ThreadSafe
    protected fun localAdd(item: IT) {
        lock.use {
            val (operation, added) = backend.localAdd(nextTime(), item)

            onLocalChange(operation.itemId, added, null)
            distribute(operation)

            trace("LOCAL ADD") { "${operation.itemId} $added" }
        }
    }

    @ThreadSafe
    protected fun localUpdate(itemId: ItemId?, updates: Collection<Pair<String, Any?>>) {
        lock.use {
            val safeItemId = itemId ?: connectionInfo.connectingHandle.itemId
            checkNotNull(safeItemId) { "no item id passed and connection info does not contain one in $this" }

            val original = backend.getItem(safeItemId)
            checkNotNull(original)

            val (operation, updated) = backend.localUpdate(nextTime(), safeItemId, updates)

            onLocalChange(operation.itemId, updated, original)
            distribute(operation)

            trace("LOCAL UPDATE") { "opTime: ${operation.itemId} diff: ${updated.diff(original).map { it.path to updated.getValue(it.path)}}" }
        }
    }

    @ThreadSafe
    protected fun localRemove(itemId: ItemId) {
        lock.use {
            val (operation, removed) = backend.localRemove(nextTime(), itemId)

            removed?.let { onLocalRemove(itemId, it) }
            distribute(operation)

            trace("LOCAL REMOVE") { "$itemId $removed" }
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

            trace("REMOTE ADD") { "${operation.itemId} $added" }
        } else {
            trace("SKIPPED :: REMOTE ADD") { "${operation.itemId}" }
        }
    }

    @RequireLock
    internal fun remoteUpdate(operation: AutoUpdate) {
        val (timestamp, original, updated) = backend.remoteUpdate(operation)

        if (timestamp != null) {
            receive(timestamp)
            onRemoteChange(operation.itemId, updated, original)
            distribute(operation)

            trace("REMOTE UPDATE") { "${operation.itemId} $updated $original" }
        } else {
            trace("SKIPPED :: REMOTE UPDATE") { "${operation.itemId}" }
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
            trace("REMOTE REMOVE") { "${operation.itemIds} $removed" }
        } else {
            trace("SKIPPED :: REMOTE REMOVE") { "${operation.itemIds}" }
        }
    }

    @RequireLock
    internal fun remoteEmpty(operation: AutoEmpty) {
        receive(operation.timestamp)
        trace("REMOTE EMPTY") { "${operation.timestamp}" }
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
        lock.use { makeConnectInfo<VT>(null, type) }

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
            AutoHandle(handle.globalId, PeerId(connectTime.timestamp), itemId)
        )
    }

    // --------------------------------------------------------------------------------
    // Peer synchronization
    // --------------------------------------------------------------------------------

    internal suspend fun syncPeer(connector: DirectConnector, connectingTime: LamportTimestamp) {
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
    protected fun receive(receivedTime: ItemId) {
        unsafeTime = unsafeTime.receive(receivedTime.value)
    }

    @RequireLock
    protected fun nextTime(): LamportTimestamp =
        unsafeTime.increment().also { unsafeTime = it }

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
        }
    }

    @ThreadSafe
    fun addListener(listener: AutoCollectionListener<IT>) {
        lock.use {
            collectionListeners += listener
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
        logger.fine { "[${callSiteName.substringAfterLast('.')} @ $time] ${builder()}" }
    }

}