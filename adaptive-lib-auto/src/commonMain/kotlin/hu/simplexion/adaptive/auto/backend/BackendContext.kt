package hu.simplexion.adaptive.auto.backend

import hu.simplexion.adaptive.atomic.Atomic
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.connector.AutoConnector
import hu.simplexion.adaptive.auto.frontend.AbstractFrontend
import hu.simplexion.adaptive.auto.model.AutoHandle
import hu.simplexion.adaptive.utility.getLock
import hu.simplexion.adaptive.utility.use
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope

class BackendContext(
    val handle: AutoHandle,
    val scope: CoroutineScope,
    val wireFormatProvider: WireFormatProvider,
    val trace: Boolean,
    time: LamportTimestamp
) {

    var time by Atomic(time)

    var frontEnd: AbstractFrontend? = null

    val connectorLock = getLock()

    private var pConnectors = listOf<AutoConnector>()

    val connectors
        get() = connectorLock.use { pConnectors }

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
}