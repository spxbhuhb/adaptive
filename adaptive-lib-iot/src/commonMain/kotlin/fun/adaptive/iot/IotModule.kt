package `fun`.adaptive.iot

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.space.AioSpaceSpec
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.builtin.AvString
import `fun`.adaptive.value.item.AmvItemIdList
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.operation.*
import `fun`.adaptive.wireformat.WireFormatRegistry

open class IotModule<WT>(
    val loadStrings: Boolean = true
) : AppModule<WT>() {

    override fun WireFormatRegistry.init() {
        this += AvoAdd
        this += AvoAddOrUpdate
        this += AvoMarkerRemove
        this += AvoUpdate
        this += AvoTransaction

        this += AvSubscribeCondition

        this += AvItem
        this += AvStatus
        this += AmvItemIdList

        this += AioSpaceSpec
        this += AioDeviceSpec

        this += AvString
    }
    
    override suspend fun loadResources() {
        if (loadStrings) {
            commonMainStringsStringStore0.load()
        }
    }

}