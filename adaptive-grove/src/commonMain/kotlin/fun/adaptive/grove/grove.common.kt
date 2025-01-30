package `fun`.adaptive.grove

import `fun`.adaptive.grove.sheet.model.SelectionInfo
import `fun`.adaptive.wireformat.WireFormatRegistry

fun groveCommon() {
    val r = WireFormatRegistry

    r += SelectionInfo
}