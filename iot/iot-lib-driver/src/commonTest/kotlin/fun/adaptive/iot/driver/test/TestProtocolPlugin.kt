package `fun`.adaptive.iot.driver.test

import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.driver.backend.AioDriverWorker
import `fun`.adaptive.iot.driver.backend.AioProtocolPlugin
import `fun`.adaptive.iot.driver.backend.protocol.FifoProtocolWorker
import `fun`.adaptive.iot.driver.request.AdrStartControllerDiscovery
import `fun`.adaptive.iot.driver.test.task.CommissionController
import `fun`.adaptive.iot.driver.test.task.CommissionNetwork
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.store.AvComputeContext

class TestProtocolPlugin : AioProtocolPlugin<TestNetworkSpec, TestControllerSpec, TestPointSpec>() {

    override lateinit var driverWorker: AioDriverWorker<TestNetworkSpec, TestControllerSpec, TestPointSpec>
    override lateinit var valueWorker: AvValueWorker

    lateinit var protocolWorker: FifoProtocolWorker<TestNetworkSpec, TestControllerSpec, TestPointSpec>

    override fun start() {
        protocolWorker = driverWorker.safeAdapter.firstImpl<FifoProtocolWorker<TestNetworkSpec, TestControllerSpec, TestPointSpec>>()
    }

    override fun commissionNetwork(cc: AvComputeContext, original: AvValue<TestNetworkSpec>?, new: AvValue<TestNetworkSpec>) {
        cc += new
        protocolWorker += CommissionNetwork(new)
    }

    override fun commissionController(cc: AvComputeContext, original: AvValue<TestControllerSpec>?, new: AvValue<TestControllerSpec>) {
        cc += new
        protocolWorker += CommissionController(new)
    }

    override fun commissionPoint(cc: AvComputeContext, original: AvValue<TestPointSpec>?, new: AvValue<TestPointSpec>) {
        cc += new
    }

    override fun startControllerDiscovery(request: AdrStartControllerDiscovery) {
    }


}