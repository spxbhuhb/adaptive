package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.model.E
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

fun cookbookCommon() {
    uiCommon()
    WireFormatRegistry.set("fun.adaptive.cookbook.model.E", EnumWireFormat(E.entries))
}