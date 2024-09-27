package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.atomic.Atomic
import `fun`.adaptive.auto.api.AutoListener
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

    val time : LamportTimestamp
        get() = safeTime.get()

    val connectors
        get() = lock.use { pConnectors }

    private val lock = getLock()

    private val safeTime = Atomic(time, lock)

    private var pConnectors = listOf<AutoConnector>()

    private var listeners = listOf<AutoListener<A>>()

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
            toRemove.forEach { it.onDisconnect() }
        }
    }

    fun addListener(listener: AutoListener<A>) {
        lock.use {
            listeners += listener
        }
    }

    fun removeListener(listener: AutoListener<A>) {
        lock.use {
            listeners -= listener
        }
    }

    suspend fun stop() {
        connectors.forEach { safeSuspendCall(logger) { it.disconnect() } }
        logger.fine("backend context stopped")
    }

    // --------------------------------------------------------------------------------
    // Listener callbacks
    // --------------------------------------------------------------------------------

    internal fun onAdd(item : A) {
        listeners.forEach { it.onAdd(item) }
    }

    internal fun onRemove(value : A) {
        listeners.forEach { it.onRemove(value) }
    }

    internal fun onListInit(values : List<A>) {
        listeners.forEach { it.onListInit(values) }
    }

    internal fun onListCommit(values : List<A>) {
        listeners.forEach { it.onListCommit(values) }
    }

    internal fun onItemCommit(item : A) {
        listeners.forEach { it.onItemCommit(item) }
    }
}