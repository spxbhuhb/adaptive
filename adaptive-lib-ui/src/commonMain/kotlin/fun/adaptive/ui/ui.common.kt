package `fun`.adaptive.ui

import `fun`.adaptive.ui.app.basic.DefaultLayoutState
import `fun`.adaptive.ui.app.basic.SidebarUserMode
import `fun`.adaptive.ui.builtin.commonMainStringsStringStore0
import `fun`.adaptive.ui.snackbar.SnackType
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

suspend fun uiCommon() {
    commonMainStringsStringStore0.load()

    WireFormatRegistry += DefaultLayoutState
    WireFormatRegistry.set("fun.adaptive.ui.app.basic.SidebarUserMode", EnumWireFormat(SidebarUserMode.entries))
    WireFormatRegistry.set("fun.adaptive.ui.snackbar.SnackType", EnumWireFormat(SnackType.entries))
}