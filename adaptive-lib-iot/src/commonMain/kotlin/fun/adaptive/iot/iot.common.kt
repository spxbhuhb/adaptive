package `fun`.adaptive.iot

import `fun`.adaptive.adaptive_lib_iot.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.iot.model.device.AioDevice
import `fun`.adaptive.iot.model.device.point.AioDevicePoint
import `fun`.adaptive.iot.model.project.AioProject
import `fun`.adaptive.iot.model.space.AioSpace
import `fun`.adaptive.iot.model.space.AioSpaceType
import `fun`.adaptive.wireformat.WireFormatRegistry

suspend fun iotCommon() {
    val r = WireFormatRegistry

    r += AioProject
    r += AioSpace
    r += AioSpaceType
    r += AioDevice
    r += AioDevicePoint

    commonMainStringsStringStore0.load()
}

