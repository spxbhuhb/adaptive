package `fun`.adaptive.ui

import `fun`.adaptive.ui.layout.app.default.AppLayoutState
import `fun`.adaptive.ui.layout.app.default.SidebarUserMode
import `fun`.adaptive.ui.snackbar.SnackType
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

fun uiCommon() {
    WireFormatRegistry += AppLayoutState
    WireFormatRegistry.set("fun.adaptive.ui.layout.app.default.SidebarUserMode", EnumWireFormat(SidebarUserMode.entries))
    WireFormatRegistry.set("fun.adaptive.ui.snackbar.SnackType", EnumWireFormat(SnackType.entries))
}