package `fun`.adaptive.iot.driver.backend

import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.store.AvComputeContext

abstract class AioProtocolPlugin<NT : AioDeviceSpec,CT: AioDeviceSpec,PT: AioPointSpec> {

    abstract var valueWorker : AvValueWorker

    open fun commissionNetwork(cc: AvComputeContext, original: AvItem<NT>?, new: AvItem<NT>) = Unit
    open suspend fun commissionController(item: AvItem<CT>) = Unit
    open suspend fun commissionPoint(item: AvItem<PT>) = Unit

    open suspend fun enableNetwork(network : AvItem<NT>) = Unit
    open suspend fun enableController(item: AvItem<CT>) = Unit
    open suspend fun enablePoint(item: AvItem<PT>) = Unit

    open suspend fun disableNetwork(network : AvItem<NT>) = Unit
    open suspend fun disableController(item: AvItem<CT>) = Unit
    open suspend fun disablePoint(item: AvItem<PT>) = Unit


}