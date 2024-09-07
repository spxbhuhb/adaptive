package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.atomic.Atomic
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.MetadataId
import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.safeSuspendCall
import `fun`.adaptive.utility.use
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope

class BackendContext(
    val handle: AutoHandle,
    val scope: CoroutineScope,
    val logger: AdaptiveLogger,
    val wireFormatProvider: WireFormatProvider,
    val defaultMetadata: AdatClassMetadata,
    val defaultWireFormat: AdatClassWireFormat<*>,
    time: LamportTimestamp
) {

    // FIXME this atomic is not really atomic
    var time by Atomic(time)

    val connectorLock = getLock()

    private var pConnectors = listOf<AutoConnector>()

    val metadata = mutableMapOf<MetadataId, AdatClassMetadata>()

    val wireFormats = mutableMapOf<ItemId, WireFormat<*>>()

    val connectors
        get() = connectorLock.use { pConnectors }

    init {
        logger.fine("backend context created")
    }

    fun receive(receivedTime: LamportTimestamp) {
        time = time.receive(receivedTime)
    }

    fun nextTime(): LamportTimestamp {
        time = time.increment()
        return time
    }

    fun addConnector(connector: AutoConnector) {
        connectorLock.use {
            pConnectors += connector
        }
    }

    fun removeConnector(handle: AutoHandle) {
        connectorLock.use {
            val toRemove = pConnectors.filter { it.peerId == handle.peerId }
            pConnectors -= toRemove
            toRemove.forEach { it.onDisconnect() }
        }
    }

    suspend fun stop() {
        connectors.forEach { safeSuspendCall(logger) { it.disconnect() } }
        logger.fine("backend context stopped")
    }
}