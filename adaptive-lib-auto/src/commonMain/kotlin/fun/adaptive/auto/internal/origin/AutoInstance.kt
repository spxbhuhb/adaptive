package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.atomic.Atomic
import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.api.AutoCollectionListener
import `fun`.adaptive.auto.api.AutoItemListener
import `fun`.adaptive.auto.api.InfoFunOrNull
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.internal.connector.DirectConnector
import `fun`.adaptive.auto.internal.connector.ServiceConnector
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.internal.frontend.AutoFrontend
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.CleanupHandler
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.safeSuspendCall
import `fun`.adaptive.utility.use
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.time.Duration

/**
 * The base class for all auto instances.
 */
open class AutoInstance<BE : AutoBackend<*>, FE : AutoFrontend<VT,IT>, VT, IT : AdatClass>(
    val defaultWireFormat: AdatClassWireFormat<*>?,
    val wireFormatProvider: WireFormatProvider
) {

    val time: LamportTimestamp
        get() = safeTime.get()

    private val lock = getLock()

    private lateinit var logger: AdaptiveLogger

    internal lateinit var handle: AutoHandle

    private lateinit var backend: BE

    private lateinit var frontend: FE

    private val safeTime = Atomic(LamportTimestamp(handle.peerId, 0), lock)

    var connectors = listOf<AutoConnector>()
        get() = lock.use { field }
        private set(value) {
            lock.use { field = value }
        }

    /**
     * Peers that are connected directly to this instance are "close".
     */
    var closePeers = emptyList<Long>()
        get() = lock.use { field }
        private set(value) {
            lock.use { field = value }
        }

    private var itemListeners = emptyList<AutoItemListener<IT>>()

    private var collectionListeners = emptyList<AutoCollectionListener<IT>>()

    init {
        logger.fine("backend context created")

        // getLogger("fun.adaptive.auto.${handle.globalId.toShort()}.${handle.peerId}").also {
        //        if (trace) it.enableFine()
        //    }


    }

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
    // Functions that connect an endpoint/node to a node
    // --------------------------------------------------------------------------------




    // --------------------------------------------------------------------------------
    // Utility functions
    // --------------------------------------------------------------------------------

    suspend fun waitForSync(connectInfo: AutoConnectionInfo<VT>, timeout: Duration) {
        backend.waitForSync(connectInfo, timeout)
    }

    open fun update(new: IT) {
        frontend.update(new)
    }

    fun update(original: IT, new: IT) {
        frontend.also {
            if (it is AdatClassListFrontend<*>) {
                @Suppress("UNCHECKED_CAST")
                it as AdatClassListFrontend<IT>
                it.update(original, new)
            }
        }
    }


    // --------------------------------------------------------------------------------
    // Time
    // --------------------------------------------------------------------------------

    fun receive(receivedTime: LamportTimestamp) {
        safeTime.compute { it.receive(receivedTime) }
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
    suspend fun addPeer(connector: AutoConnector, peerTime: LamportTimestamp): LamportTimestamp {

        lock.use {
            connectors += connector
            closePeers += connector.peerHandle.peerId
        }

        supervisorScope { launch { backend.syncPeer(connector, peerTime, null) } }

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

    fun closePeers() = lock.use { connectors.forEach { it.dispose() } }

    // --------------------------------------------------------------------------------
    // Listeners
    // --------------------------------------------------------------------------------


    fun addListener(listener: AutoItemListener<IT>) {
        lock.use {
            itemListeners += listener
        }
    }

    fun removeListener(listener: AutoItemListener<IT>) {
        lock.use {
            itemListeners -= listener
        }
    }

    fun addListener(listener: AutoCollectionListener<IT>) {
        lock.use {
            collectionListeners += listener
        }
    }

    fun removeListener(listener: AutoCollectionListener<IT>) {
        lock.use {
            collectionListeners -= listener
        }
    }


    suspend fun stop() {
        connectors.forEach { safeSuspendCall(logger) { it.disconnect() } }
        logger.fine("backend context stopped")
    }

    // --------------------------------------------------------------------------------
    // Commit
    // --------------------------------------------------------------------------------


    // --------------------------------------------------------------------------------
    // Listener callbacks
    // --------------------------------------------------------------------------------

    internal fun onChange(newValue: IT, oldValue: IT?, fromBackend: Boolean) {
        itemListeners.forEach { if (fromBackend || ! it.backendOnly) it.onChange(newValue, oldValue) }
        collectionListeners.forEach { if (fromBackend || ! it.backendOnly) it.onChange(newValue, oldValue) }
    }

    internal fun onInit(values: List<IT>, fromBackend: Boolean) {
        collectionListeners.forEach { if (fromBackend || ! it.backendOnly) it.onInit(values) }
    }

    internal fun onChange(values: List<IT>, fromBackend: Boolean) {
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
//
//// --------------------------------------------------------------------------------
//// Builder
//// --------------------------------------------------------------------------------
//
//fun build(builder: AutoInstanceBuilder.() -> Unit) {
//    builder(AutoInstanceBuilder())
//}
//
//class AutoInstanceBuilder<BE : AutoBackend, FE : AutoFrontend, VT, IT : AdatClass> {
//
//    fun backend(builder: (instance: AutoInstance<BE, FE, VT, IT>) -> BE) {
//        backend = builder(this@AutoInstance)
//    }
//
//    fun frontend(builder: (instance: AutoInstance<BE, FE, VT, IT>) -> FE) {
//        frontend = builder(this@AutoInstance)
//    }
//
//}