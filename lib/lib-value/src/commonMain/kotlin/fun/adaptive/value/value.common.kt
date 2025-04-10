package `fun`.adaptive.value

import `fun`.adaptive.value.builtin.AvBoolean
import `fun`.adaptive.value.builtin.AvConvertedDouble
import `fun`.adaptive.value.builtin.AvDouble
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.item.AvRefList
import `fun`.adaptive.value.builtin.AvString
import `fun`.adaptive.value.operation.*
import `fun`.adaptive.wireformat.WireFormatRegistry

fun valueCommon(loadStrings: Boolean = true) {
    val r = WireFormatRegistry

    r += AvoAdd
    r += AvoAddOrUpdate
    r += AvoMarkerRemove
    r += AvoUpdate
    r += AvoTransaction

    r += AvSubscribeCondition

    r += AvItem
    r += AvStatus
    r += AvRefList

    r += AvString
    r += AvDouble
    r += AvConvertedDouble
    r += AvBoolean
}