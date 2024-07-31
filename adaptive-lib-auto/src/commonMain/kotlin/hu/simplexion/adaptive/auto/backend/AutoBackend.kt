package hu.simplexion.adaptive.auto.backend

import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.connector.AutoConnector
import hu.simplexion.adaptive.utility.UUID
import kotlinx.atomicfu.*

abstract class AutoBackend(
    time: LamportTimestamp
) : AutoConnector() {

    abstract val globalId: UUID<AutoBackend>

    private val pConnectors = atomic<List<AutoConnector>>(listOf())

    val connectors
        get() = pConnectors.value

    var time = time
        protected set

    fun nextTime(): LamportTimestamp {
        time = time.increment()
        return time
    }

    operator fun plusAssign(connector: AutoConnector) {
        pConnectors.value = pConnectors.value + connector
    }

}