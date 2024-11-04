package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.atomic.Atomic
import `fun`.adaptive.auto.api.AutoCollectionListener
import `fun`.adaptive.auto.api.AutoItemListener
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.backend.AutoItemBackend
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.internal.frontend.AutoFrontend
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.PeerId
import `fun`.adaptive.auto.model.operation.AutoOperation
import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.safeSuspendCall
import `fun`.adaptive.utility.use
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.time.Duration

/**
 * The base class for all auto instances.
 */
abstract class AutoInstance<BE : AutoBackend<IT>, FE : AutoFrontend<VT, IT>, VT, IT : AdatClass>(
    val defaultWireFormat: AdatClassWireFormat<*>?,
    val wireFormatProvider: WireFormatProvider,
    val scope: CoroutineScope,
) {

    val time: LamportTimestamp
        get() = safeTime.get()

    private val lock = getLock()

    internal var logger: AdaptiveLogger = getLogger("auto.*")
        set(v) {
            field.disconnect()
            field = v
        }

    internal lateinit var connectionInfo: AutoConnectionInfo<VT>
        private set

    internal lateinit var handle: AutoHandle
        private set

    internal lateinit var backend: BE

    internal lateinit var frontend: FE

    private val safeTime = Atomic(LamportTimestamp(handle.peerId, 0), lock)

    var connectors = listOf<AutoConnector>()
        get() = lock.use { field }
        private set(value) {
            lock.use { field = value }
        }

    /**
     * Peers that are connected directly to this instance are "close".
     */
    var closePeers = emptyList<PeerId>()
        get() = lock.use { field }
        private set(value) {
            lock.use { field = value }
        }

    private var itemListeners = emptyList<AutoItemListener<IT>>()

    private var collectionListeners = emptyList<AutoCollectionListener<IT>>()

    // --------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------

    internal fun setInfo(connectionInfo: AutoConnectionInfo<VT>, worker: AutoWorker?) {
        this.connectionInfo = connectionInfo
        handle = connectionInfo.connectingHandle

        val loggerName = "auto.${handle.globalId.toShort()}.${handle.peerId}${handle.itemId?.let { ".$it" } ?: ""}"
        logger = getLogger(loggerName)

        if (connectionInfo.connectionType == AutoConnectionType.Service) {
            checkNotNull(worker) { "cannot register auto instance without a worker ($this)" }
            worker.register(this)
        }
    }

    suspend fun stop() {
        connectors.forEach { safeSuspendCall(logger) { it.disconnect() } }
        logger.fine("backend context stopped")
    }

    suspend fun waitForSync(connectInfo: AutoConnectionInfo<VT>, timeout: Duration) {
        backend.waitForSync(connectInfo, timeout)
    }

    // --------------------------------------------------------------------------------
    // Communication
    // --------------------------------------------------------------------------------

    fun receive(operation: AutoOperation) {
        backend.receive(operation)
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
    abstract fun commit(itemBackend : AutoItemBackend<IT>?, initial: Boolean, fromPeer: Boolean)

    // --------------------------------------------------------------------------------
    // Functions to get connection info from this node
    // --------------------------------------------------------------------------------

    fun connectInfo(type: AutoConnectionType = AutoConnectionType.Service): AutoConnectionInfo<VT> {
        @Suppress("UNCHECKED_CAST")
        return backend.connectInfo(type) as AutoConnectionInfo<VT>
    }

    fun <IT> connectInfo(itemId: ItemId, type: AutoConnectionType = AutoConnectionType.Service): AutoConnectionInfo<IT> {
        @Suppress("UNCHECKED_CAST")
        return backend.connectInfo(type, itemId) as AutoConnectionInfo<IT>
    }

    @Suppress("UNCHECKED_CAST")
    fun <IT> connectInfo(type: AutoConnectionType = AutoConnectionType.Service, filterFun: (IT) -> Boolean): AutoConnectionInfo<IT>? {
        val safeFrontend = frontend
        check(safeFrontend is AdatClassListFrontend<*>) { "connecting with filter function is supported only by AdatClassListFrontend" }
        val itemId = safeFrontend.values.firstOrNull { filterFun(it as IT) }?.let { safeFrontend.itemId(it) } ?: return null
        return backend.connectInfo(type, itemId) as AutoConnectionInfo<IT>
    }


    // --------------------------------------------------------------------------------
    // Time
    // --------------------------------------------------------------------------------

    fun receive(receivedTime: LamportTimestamp) {
        safeTime.compute { it.receive(receivedTime) }
    }

    fun receive(receivedTime: ItemId) {
        safeTime.compute { it.receive(receivedTime.asLamportTimestamp()) }
    }

    fun nextTime(): LamportTimestamp =
        safeTime.compute { it.increment() }

    // --------------------------------------------------------------------------------
    // Peers
    // --------------------------------------------------------------------------------

    /**
     * Add a connection to a peer. The instance launches `backend.syncPeer` to send all known
     * operations that happened after the [peerTime] to the peer.
     */
    fun addPeer(connector: AutoConnector, peerTime: LamportTimestamp): LamportTimestamp {

        lock.use {
            connectors += connector
            closePeers += connector.peerHandle.peerId
        }

        scope.launch {
            supervisorScope {
                launch { backend.syncPeer(connector, peerTime, null) }
            }
        }

        return time
    }

    fun removePeer(handle: AutoHandle) {
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

    internal fun onChange(newValue: IT, oldValue: IT?, fromBackend: Boolean) {
        itemListeners.forEach { if (fromBackend || ! it.backendOnly) it.onChange(newValue, oldValue) }
        collectionListeners.forEach { if (fromBackend || ! it.backendOnly) it.onChange(newValue, oldValue) }
    }

    internal fun onInit(values: Collection<IT>, fromBackend: Boolean) {
        collectionListeners.forEach { if (fromBackend || ! it.backendOnly) it.onInit(values) }
    }

    internal fun onChange(values: Collection<IT>, fromBackend: Boolean) {
        collectionListeners.forEach { if (fromBackend || ! it.backendOnly) it.onChange(values) }
    }

    internal fun onRemove(value: IT, fromBackend: Boolean) {
        collectionListeners.forEach { if (fromBackend || ! it.backendOnly) it.onRemove(value) }
    }

    internal fun onSyncEnd(fromBackend: Boolean) {
        collectionListeners.forEach { if (fromBackend || ! it.backendOnly) it.onSyncEnd() }
    }

    // --------------------------------------------------------------------------------
    // Logging
    // --------------------------------------------------------------------------------

    open fun error(message: String) {
        logger.error(message)
    }

    open fun trace(callSiteName: String, builder: () -> String) {
        logger.fine { "[${callSiteName.substringAfterLast('.')} @ $time] ${builder()}" }
    }

}