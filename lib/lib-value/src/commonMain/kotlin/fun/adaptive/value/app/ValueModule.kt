package `fun`.adaptive.value.app

import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.value.AvRefListSpec
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.operation.*
import `fun`.adaptive.wireformat.WireFormatRegistry

abstract class ValueModule<FW : AbstractWorkspace, BW : AbstractWorkspace> : AppModule<FW,BW>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + AvoAdd
        + AvoAddOrUpdate
        + AvoMarkerRemove
        + AvoUpdate
        + AvoTransaction

        + AvSubscribeCondition
        + AvValue
        + AvRefListSpec
    }

}