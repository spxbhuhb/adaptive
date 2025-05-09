package `fun`.adaptive.iot.driver.backend

import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.driver.request.AdrStartControllerDiscovery
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.store.AvComputeContext

abstract class AioProtocolPlugin<NT : AioDeviceSpec,CT: AioDeviceSpec,PT: AioPointSpec> {

    abstract var driverWorker : AioDriverWorker<NT,CT,PT>
    abstract var valueWorker : AvValueWorker

    open fun start() = Unit
    open fun stop() = Unit

    open fun commissionNetwork(cc: AvComputeContext, original: AvValue<NT>?, new: AvValue<NT>) = Unit
    open fun commissionController(cc: AvComputeContext, original: AvValue<CT>?, new: AvValue<CT>) = Unit
    open fun commissionPoint(cc: AvComputeContext, original: AvValue<PT>?, new: AvValue<PT>) = Unit

    open fun startControllerDiscovery(request : AdrStartControllerDiscovery) = Unit

    open suspend fun enableNetwork(network: AvValue<NT>) = Unit
    open suspend fun enableController(item: AvValue<CT>) = Unit
    open suspend fun enablePoint(item: AvValue<PT>) = Unit

    open suspend fun disableNetwork(network: AvValue<NT>) = Unit
    open suspend fun disableController(item: AvValue<CT>) = Unit
    open suspend fun disablePoint(item: AvValue<PT>) = Unit

    open suspend fun writePoint(
        controller: AvValue<CT>,
        point: AvValue<PT>,
        value: Any?
    ) {
        unsupported()
    }

    open suspend fun readPoint(
        controller: AvValue<CT>,
        point: AvValue<PT>
    ) {
        unsupported()
    }

    open suspend fun ping(controller: AvValue<CT>) {
        unsupported()
    }
}