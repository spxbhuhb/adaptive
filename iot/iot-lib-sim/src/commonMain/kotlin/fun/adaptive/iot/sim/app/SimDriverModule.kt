package `fun`.adaptive.iot.sim.app

import `fun`.adaptive.iot.sim.spec.SimControllerSpec
import `fun`.adaptive.iot.sim.spec.SimNetworkSpec
import `fun`.adaptive.iot.sim.spec.SimPointSpec
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.wireformat.WireFormatRegistry

open class SimDriverModule<WT : AbstractWorkspace> : AppModule<WT>() {

    val driverKey: String
        get() = "aio:driver:sim"

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + SimNetworkSpec
        + SimControllerSpec
        + SimPointSpec
    }

}