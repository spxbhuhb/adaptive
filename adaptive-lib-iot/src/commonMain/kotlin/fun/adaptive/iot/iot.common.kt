package `fun`.adaptive.iot

import `fun`.adaptive.adaptive_lib_iot.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.iot.model.project.AioProject
import `fun`.adaptive.iot.model.space.AioSpace
import `fun`.adaptive.iot.model.space.AioSpaceType
import `fun`.adaptive.wireformat.WireFormatRegistry

suspend fun iotCommon() {
    val r = WireFormatRegistry

    r += AioProject
    r += AioSpace
    r += AioSpaceType

    commonMainStringsStringStore0.load()
}