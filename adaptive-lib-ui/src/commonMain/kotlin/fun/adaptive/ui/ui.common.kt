package `fun`.adaptive.ui

import `fun`.adaptive.ui.snackbar.SnackType
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

fun uiCommon() {
    WireFormatRegistry.set("fun.adaptive.ui.snackbar.SnackType", EnumWireFormat(SnackType.entries))
}