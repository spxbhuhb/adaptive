package `fun`.adaptive.value.app

import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.builtin.AvBoolean
import `fun`.adaptive.value.builtin.AvConvertedDouble
import `fun`.adaptive.value.builtin.AvDouble
import `fun`.adaptive.value.builtin.AvString
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItemIdList
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.operation.*
import `fun`.adaptive.wireformat.WireFormatRegistry

abstract class ValueModule<WT : AbstractWorkspace> : AppModule<WT>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + AvoAdd
        + AvoAddOrUpdate
        + AvoMarkerRemove
        + AvoUpdate
        + AvoTransaction

        + AvSubscribeCondition

        + AvItem
        + AvStatus
        + AvItemIdList

        + AvString
        + AvDouble
        + AvConvertedDouble
        + AvBoolean
    }

}