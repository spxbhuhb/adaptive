package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.api.AutoCollectionListener
import `fun`.adaptive.auto.api.AutoItemListener
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.backend.AutoCollectionBackend
import `fun`.adaptive.auto.internal.backend.AutoItemBackend
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.internal.persistence.AutoCollectionExport
import `fun`.adaptive.auto.internal.persistence.AutoItemExport
import `fun`.adaptive.auto.internal.persistence.AutoItemPersistence
import `fun`.adaptive.auto.internal.persistence.AutoPersistence
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.PeerId
import `fun`.adaptive.auto.model.operation.AutoAdd
import `fun`.adaptive.auto.model.operation.AutoEmpty
import `fun`.adaptive.auto.model.operation.AutoUpdate
import `fun`.adaptive.auto.model.operation.AutoOperation
import `fun`.adaptive.auto.model.operation.AutoRemove
import `fun`.adaptive.auto.model.operation.AutoSyncBatch
import `fun`.adaptive.auto.model.operation.AutoSyncEnd
import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.RequireLock
import `fun`.adaptive.utility.ThreadSafe
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
    val scope: CoroutineScope,
) {

    val time: LamportTimestamp
        get() = lock.use { pTime }

    private val lock = getLock()

    private var logger: AdaptiveLogger = getLogger("auto.*")

    private lateinit var connectionInfo: AutoConnectionInfo<VT>

    private lateinit var handle: AutoHandle

    private lateinit var backend: BE

    internal lateinit var persistence: PT

    private var pTime = LamportTimestamp(handle.peerId, 0)

    private var closePeers = emptyList<PeerId>()

    private var connectors = listOf<AutoConnector>()

    private var itemListeners = emptyList<AutoItemListener<IT>>()

    private var collectionListeners = emptyList<AutoCollectionListener<IT>>()

    // --------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------

    @ThreadSafe
    internal fun setInfo(connectionInfo: AutoConnectionInfo<VT>, worker: AutoWorker?) {
        lock.use {
            this.connectionInfo = connectionInfo
            handle = connectionInfo.connectingHandle

            val loggerName = "auto.${handle.globalId.toShort()}.${handle.peerId}${handle.itemId?.let { ".$it" } ?: ""}"
            logger = getLogger(loggerName)
        }

        if (connectionInfo.connectionType == AutoConnectionType.Service) {
            checkNotNull(worker) { "cannot register auto instance without a worker ($this)" }
            worker.register(this)
        }
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
    // Interface provided for users of this instance
    // --------------------------------------------------------------------------------

    @ThreadSafe
    protected fun localAdd(item: IT) {
        lock.use {
            val (operation, added) = backend.add(nextTime(), item)
            onChange(operation.itemId, added, null, fromPeer = false)
            distribute(operation)
        }
    }

    @ThreadSafe
    protected fun localUpdate(itemId: ItemId, updates: Collection<Pair<String, Any?>>) {
        lock.use {
            val original = backend.getItem(itemId)
            val (operation, updated) = backend.update(nextTime(), itemId, updates)
            onChange(operation.itemId, updated, original, fromPeer = false)
            distribute(operation)
        }
    }

    @ThreadSafe
    protected fun localRemove(itemId: ItemId) {
        lock.use {
            val (operation, removed) = backend.remove(nextTime(), itemId)
            removed?.let { onRemove(it, fromPeer = false) }
            distribute(operation)
        }
    }

    // --------------------------------------------------------------------------------
    // Operations from peers
    // --------------------------------------------------------------------------------

    @ThreadSafe
    internal fun receive(operation: AutoOperation) {
        lock.use {
            operation.apply(this)
        }
    }

    @RequireLock
    internal open fun add(operation: AutoAdd) {
        val (timestamp, added) = backend.add(operation)

        if (timestamp != null) {
            receive(timestamp)
            onChange(operation.itemId, added, null, fromPeer = true)
            distribute(operation)
        }
    }

    @RequireLock
    internal fun update(operation: AutoUpdate) {
        val (timestamp, original, updated) = backend.update(operation)

        if (timestamp != null) {
            receive(timestamp)
            onChange(operation.itemId, updated, original, fromPeer = true)
            distribute(operation)
        }
    }

    @RequireLock
    internal open fun remove(operation: AutoRemove) {
        val (timestamp, removed) = backend.remove(operation)
        if (timestamp != null) {
            receive(timestamp)
            if (removed != null) {
                onRemove(removed, fromPeer = true)
            }
            distribute(operation)
        }
    }

    @RequireLock
    internal fun empty(operation: AutoEmpty) {
        receive(operation.timestamp)
        // FIXME AutoEmpty
//        onInit(emptyList(), fromBackend = true)
//        onSyncEnd(fromBackend = true)
    }

    @RequireLock
    internal open fun syncBatch(operation: AutoSyncBatch) {
        operation.additions.forEach { it.apply(this) }
        operation.updates.forEach { it.apply(this) }
    }

    @RequireLock
    internal open fun syncEnd(operation: AutoSyncEnd) {
        backend.syncEnd(operation)
        receive(operation.timestamp)
        onSyncEnd(fromPeer = true)
    }

    /**
     * Called by backends when the data handled by the backend has been modified.
     *
     * @param  itemBackend   The item backend that initiates this commit. Null when the commit
     *                       is for a collection operation (add or remove).
     * @param  initial       True when this is the commit happening during the initialization
     *                       of the auto instance.
     * @param  fromPeer      True when this operation is received from the peer (to separate
     *                       from ones initiated by the frontend). Used to skip callback
     *                       echo to the frontend (if the backendOnly parameter is true for
     *                       the listener).
     */
    @RequireLock
    internal abstract fun commit(itemBackend: AutoItemBackend<IT>?, initial: Boolean, fromPeer: Boolean)

    @RequireLock
    private fun distribute(operation: AutoOperation) {
        val connectors = connectors
        val closePeers = closePeers
        val thisPeer = handle.peerId

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
            AutoHandle(handle.globalId, PeerId(connectTime.timestamp), null)
        )
    }

    // --------------------------------------------------------------------------------
    // Time
    // --------------------------------------------------------------------------------

    @RequireLock
    protected fun receive(receivedTime: LamportTimestamp) {
        pTime = pTime.receive(receivedTime)
    }

    @RequireLock
    protected fun receive(receivedTime: ItemId) {
        pTime = pTime.receive(receivedTime.value)
    }

    @RequireLock
    protected fun nextTime(): LamportTimestamp =
        pTime.increment().also { pTime = it }

    // --------------------------------------------------------------------------------
    // Peers
    // --------------------------------------------------------------------------------

    /**
     * Add a connection to a peer. The instance launches `backend.syncPeer` to send all known
     * operations that happened after the [peerTime] to the peer.
     */
    fun addConnector(connector: AutoConnector): LamportTimestamp {

        lock.use {
            connectors += connector
            closePeers += connector.peerHandle.peerId
        }

        return time
    }

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

    fun addListener(listener: AutoItemListener<IT>) {
        lock.use {
            itemListeners += listener
        }
    }

    fun addListener(listener: AutoCollectionListener<IT>) {
        lock.use {
            collectionListeners += listener
        }
    }

    // --------------------------------------------------------------------------------
    // Listener callbacks
    // --------------------------------------------------------------------------------

    internal fun onChange(itemId: ItemId, newValue: IT, oldValue: IT?, fromPeer: Boolean) {
        itemListeners.forEach { if (fromPeer || ! it.backendOnly) it.onChange(itemId, newValue, oldValue) }
        collectionListeners.forEach { if (fromPeer || ! it.backendOnly) it.onChange(newValue, oldValue) }
    }

    internal fun onInit(values: Collection<IT>, fromBackend: Boolean) {
        collectionListeners.forEach { if (fromBackend || ! it.backendOnly) it.onInit(values) }
    }

    internal fun onChange(values: Collection<IT>, fromBackend: Boolean) {
        collectionListeners.forEach { if (fromBackend || ! it.backendOnly) it.onChange(values) }
    }

    internal fun onRemove(value: IT, fromPeer: Boolean) {
        collectionListeners.forEach { if (fromPeer || ! it.backendOnly) it.onRemove(value) }
    }

    internal fun onSyncEnd(fromPeer: Boolean) {
        collectionListeners.forEach { if (fromPeer || ! it.backendOnly) it.onSyncEnd() }
    }

    // --------------------------------------------------------------------------------
    // Logging
    // --------------------------------------------------------------------------------

    open fun trace(callSiteName: String, builder: () -> String) {
        logger.fine { "[${callSiteName.substringAfterLast('.')} @ $time] ${builder()}" }
    }

}