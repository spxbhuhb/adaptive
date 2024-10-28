package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.atomic.Atomic
import `fun`.adaptive.auto.api.AutoCollectionListener
import `fun`.adaptive.auto.api.AutoItemListener
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.safeSuspendCall
import `fun`.adaptive.utility.use
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class BackendContext<A : AdatClass>(
    val handle: AutoHandle,
    scope: CoroutineScope?,
    val logger: AdaptiveLogger,
    val wireFormatProvider: WireFormatProvider,
    val defaultWireFormat: AdatClassWireFormat<*>?,
    time: LamportTimestamp
) {

    val scope = scope ?: CoroutineScope(Dispatchers.Default)

    val time: LamportTimestamp
        get() = safeTime.get()

    val connectors
        get() = lock.use { pConnectors }

    private val lock = getLock()

    private val safeTime = Atomic(time, lock)

    private var pConnectors = listOf<AutoConnector>()

    private var itemListeners = emptyList<AutoItemListener<A>>()

    private var collectionListeners = emptyList<AutoCollectionListener<A>>()

    init {
        logger.fine("backend context created")
    }

    fun receive(receivedTime: LamportTimestamp) {
        safeTime.compute { it.receive(receivedTime) }
    }

    fun nextTime(): LamportTimestamp =
        safeTime.compute { it.increment() }

    fun addConnector(connector: AutoConnector) {
        lock.use {
            pConnectors += connector
        }
    }

    fun removeConnector(handle: AutoHandle) {
        lock.use {
            val toRemove = pConnectors.filter { it.peerHandle.peerId == handle.peerId }
            pConnectors -= toRemove
            toRemove.forEach {
                logger.fine { "remove connector $handle" }
                it.dispose()
            }
        }
    }

    fun addListener(listener: AutoItemListener<A>) {
        lock.use {
            itemListeners += listener
        }
    }

    fun removeListener(listener: AutoItemListener<A>) {
        lock.use {
            itemListeners -= listener
        }
    }

    fun addListener(listener: AutoCollectionListener<A>) {
        lock.use {
            collectionListeners += listener
        }
    }

    fun removeListener(listener: AutoCollectionListener<A>) {
        lock.use {
            collectionListeners -= listener
        }
    }


    suspend fun stop() {
        connectors.forEach { safeSuspendCall(logger) { it.disconnect() } }
        logger.fine("backend context stopped")
    }

    // --------------------------------------------------------------------------------
    // Listener callbacks
    // --------------------------------------------------------------------------------

    internal fun onChange(newValue: A, oldValue: A?, fromBackend: Boolean) {
        itemListeners.forEach { if (fromBackend || ! it.backendOnly) it.onChange(newValue, oldValue) }
        collectionListeners.forEach { if (fromBackend || ! it.backendOnly) it.onChange(newValue, oldValue) }
    }

    internal fun onInit(values: List<A>, fromBackend: Boolean) {
        collectionListeners.forEach { if (fromBackend || ! it.backendOnly) it.onInit(values) }
    }

    internal fun onChange(values: List<A>, fromBackend: Boolean) {
        collectionListeners.forEach { if (fromBackend || ! it.backendOnly) it.onChange(values) }
    }

    internal fun onRemove(value: A, fromBackend: Boolean) {
        collectionListeners.forEach { if (fromBackend || ! it.backendOnly) it.onRemove(value) }
    }

    internal fun onSyncEnd(fromBackend: Boolean) {
        collectionListeners.forEach { if (fromBackend || ! it.backendOnly) it.onSyncEnd() }
    }
}