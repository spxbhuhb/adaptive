package `fun`.adaptive.ui

import `fun`.adaptive.ui.app.basic.DefaultLayoutState
import `fun`.adaptive.ui.app.basic.SidebarUserMode
import `fun`.adaptive.ui.snackbar.SnackType
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

fun uiCommon() {
    WireFormatRegistry += DefaultLayoutState
    WireFormatRegistry.set("fun.adaptive.ui.app.basic.SidebarUserMode", EnumWireFormat(SidebarUserMode.entries))
    WireFormatRegistry.set("fun.adaptive.ui.snackbar.SnackType", EnumWireFormat(SnackType.entries))
}