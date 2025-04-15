package `fun`.adaptive.iot.sim.driver

import `fun`.adaptive.foundation.query.first
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.driver.backend.AioDriverWorker
import `fun`.adaptive.iot.driver.backend.AioProtocolPlugin
import `fun`.adaptive.iot.driver.request.AdrStartControllerDiscovery
import `fun`.adaptive.iot.sim.spec.SimControllerSpec
import `fun`.adaptive.iot.sim.spec.SimNetworkSpec
import `fun`.adaptive.iot.sim.spec.SimPointSpec
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.store.AvComputeContext

class SimProtocolPlugin : AioProtocolPlugin<SimNetworkSpec, SimControllerSpec, SimPointSpec>() {

    override lateinit var driverWorker: AioDriverWorker<SimNetworkSpec, SimControllerSpec, SimPointSpec>
    override lateinit var valueWorker: AvValueWorker

    lateinit var simWorker: SimProtocolWorker

    override fun start() {
        simWorker = driverWorker.safeAdapter.firstImpl<SimProtocolWorker>()
    }

    override fun commissionNetwork(cc: AvComputeContext, original: AvItem<SimNetworkSpec>?, new: AvItem<SimNetworkSpec>) {
        cc += new
    }

    override fun commissionController(cc: AvComputeContext, original: AvItem<SimControllerSpec>?, new: AvItem<SimControllerSpec>) {
        cc += new
    }

    override fun commissionPoint(cc: AvComputeContext, original: AvItem<SimPointSpec>?, new: AvItem<SimPointSpec>) {
        cc += new
    }

    override fun startControllerDiscovery(request: AdrStartControllerDiscovery) {
    }


}