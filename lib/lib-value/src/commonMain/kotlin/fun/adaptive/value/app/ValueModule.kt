package `fun`.adaptive.value.app

import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.builtin.AvBoolean
import `fun`.adaptive.value.builtin.AvConvertedDouble
import `fun`.adaptive.value.builtin.AvDouble
import `fun`.adaptive.value.builtin.AvString
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.item.AvRefList
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.local.AvMarkedValueId
import `fun`.adaptive.value.local.AvMarkedValueSubscriptionResult
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

        + AvValue
        + AvStatus
        + AvRefList

        + AvString
        + AvDouble
        + AvConvertedDouble
        + AvBoolean

        + AvMarkedValueId
        + AvMarkedValueSubscriptionResult
    }

}