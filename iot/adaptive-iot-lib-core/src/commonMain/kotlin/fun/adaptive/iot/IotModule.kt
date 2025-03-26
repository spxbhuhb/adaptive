package `fun`.adaptive.iot

import `fun`.adaptive.iot.device.virtual.AioVirtualDeviceSpec
import `fun`.adaptive.iot.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.iot.history.model.*
import `fun`.adaptive.iot.point.computed.AioComputedPointSpec
import `fun`.adaptive.iot.point.sim.AioSimPointSpec
import `fun`.adaptive.iot.space.AioSpaceSpec
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.builtin.AvString
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItemIdList
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
        this += AvItemIdList

        this += AioSpaceSpec
        this += AioVirtualDeviceSpec
        this += AioComputedPointSpec
        this += AioSimPointSpec

        this += AvString

        this += AioDoubleHistoryRecord
        this += AioBooleanHistoryRecord
        this += AioStringHistoryRecord
        this += AioHistoryMetadata
        this += AioHistoryQuery
    }
    
    override suspend fun loadResources() {
        if (loadStrings) {
            commonMainStringsStringStore0.load()
        }
    }

}