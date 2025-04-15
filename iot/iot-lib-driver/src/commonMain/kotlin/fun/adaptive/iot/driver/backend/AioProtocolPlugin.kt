package `fun`.adaptive.iot.driver.backend

import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.driver.request.AdrStartControllerDiscovery
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.store.AvComputeContext

abstract class AioProtocolPlugin<NT : AioDeviceSpec,CT: AioDeviceSpec,PT: AioPointSpec> {

    abstract var driverWorker : AioDriverWorker<NT,CT,PT>
    abstract var valueWorker : AvValueWorker

    open fun start() = Unit
    open fun stop() = Unit

    open fun commissionNetwork(cc: AvComputeContext, original: AvItem<NT>?, new: AvItem<NT>) = Unit
    open fun commissionController(cc: AvComputeContext, original: AvItem<CT>?, new: AvItem<CT>) = Unit
    open fun commissionPoint(cc: AvComputeContext, original: AvItem<PT>?, new: AvItem<PT>) = Unit

    open fun startControllerDiscovery(request : AdrStartControllerDiscovery) = Unit

    open suspend fun enableNetwork(network : AvItem<NT>) = Unit
    open suspend fun enableController(item: AvItem<CT>) = Unit
    open suspend fun enablePoint(item: AvItem<PT>) = Unit

    open suspend fun disableNetwork(network : AvItem<NT>) = Unit
    open suspend fun disableController(item: AvItem<CT>) = Unit
    open suspend fun disablePoint(item: AvItem<PT>) = Unit

    open suspend fun writePoint(
        controller: AvItem<CT>,
        point: AvItem<PT>,
        value: Any?
    ) {
        unsupported()
    }

    open suspend fun readPoint(
        controller: AvItem<CT>,
        point: AvItem<PT>
    ) {
        unsupported()
    }

    open suspend fun ping(controller: AvItem<CT>) {
        unsupported()
    }
}