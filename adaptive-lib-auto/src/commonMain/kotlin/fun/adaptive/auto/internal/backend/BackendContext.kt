package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.atomic.Atomic
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.safeSuspendCall
import `fun`.adaptive.utility.use
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope

class BackendContext(
    val handle: AutoHandle,
    val scope: CoroutineScope?,
    val logger: AdaptiveLogger,
    val wireFormatProvider: WireFormatProvider,
    val defaultWireFormat: AdatClassWireFormat<*>?,
    time: LamportTimestamp
) {

    private val safeTime = Atomic(time)

    val time : LamportTimestamp
        get() = safeTime.get()

    val connectorLock = getLock()

    private var pConnectors = listOf<AutoConnector>()

    val connectors
        get() = connectorLock.use { pConnectors }

    init {
        logger.fine("backend context created")
    }

    fun receive(receivedTime: LamportTimestamp) {
        safeTime.compute { it.receive(receivedTime) }
    }

    fun nextTime(): LamportTimestamp =
        safeTime.compute { it.increment() }

    fun addConnector(connector: AutoConnector) {
        connectorLock.use {
            pConnectors += connector
        }
    }

    fun removeConnector(handle: AutoHandle) {
        connectorLock.use {
            val toRemove = pConnectors.filter { it.peerHandle.peerId == handle.peerId }
            pConnectors -= toRemove
            toRemove.forEach { it.onDisconnect() }
        }
    }

    suspend fun stop() {
        connectors.forEach { safeSuspendCall(logger) { it.disconnect() } }
        logger.fine("backend context stopped")
    }
}