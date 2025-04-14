package `fun`.adaptive.iot.sim.driver

import `fun`.adaptive.iot.driver.backend.AioProtocolPlugin
import `fun`.adaptive.iot.sim.spec.SimControllerSpec
import `fun`.adaptive.iot.sim.spec.SimNetworkSpec
import `fun`.adaptive.iot.sim.spec.SimPointSpec
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.store.AvComputeContext

class SimProtocolPlugin : AioProtocolPlugin<SimNetworkSpec, SimControllerSpec, SimPointSpec>() {

    override lateinit var valueWorker: AvValueWorker
    lateinit var simWorker: SimProtocolWorker

    override fun commissionNetwork(
        cc: AvComputeContext,
        original: AvItem<SimNetworkSpec>?,
        new: AvItem<SimNetworkSpec>
    ) {
        cc += new
    }

}