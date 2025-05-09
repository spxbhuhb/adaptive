package `fun`.adaptive.iot.sim.driver

import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.driver.backend.AioDriverWorker
import `fun`.adaptive.iot.driver.backend.AioProtocolPlugin
import `fun`.adaptive.iot.driver.backend.protocol.FifoProtocolWorker
import `fun`.adaptive.iot.driver.request.AdrStartControllerDiscovery
import `fun`.adaptive.iot.sim.spec.SimControllerSpec
import `fun`.adaptive.iot.sim.spec.SimNetworkSpec
import `fun`.adaptive.iot.sim.spec.SimPointSpec
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.store.AvComputeContext

class SimProtocolPlugin : AioProtocolPlugin<SimNetworkSpec, SimControllerSpec, SimPointSpec>() {

    override lateinit var driverWorker: AioDriverWorker<SimNetworkSpec, SimControllerSpec, SimPointSpec>
    override lateinit var valueWorker: AvValueWorker

    lateinit var simWorker: FifoProtocolWorker<SimNetworkSpec, SimControllerSpec, SimPointSpec>

    override fun start() {
        simWorker = driverWorker.safeAdapter.firstImpl<FifoProtocolWorker<SimNetworkSpec, SimControllerSpec, SimPointSpec>>()
    }

    override fun commissionNetwork(cc: AvComputeContext, original: AvValue<SimNetworkSpec>?, new: AvValue<SimNetworkSpec>) {
        cc += new
    }

    override fun commissionController(cc: AvComputeContext, original: AvValue<SimControllerSpec>?, new: AvValue<SimControllerSpec>) {
        cc += new
    }

    override fun commissionPoint(cc: AvComputeContext, original: AvValue<SimPointSpec>?, new: AvValue<SimPointSpec>) {
        cc += new
    }

    override fun startControllerDiscovery(request: AdrStartControllerDiscovery) {
    }


}