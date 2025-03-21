package `fun`.adaptive.ui.app.ws

import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.builtin.power_settings_new
import `fun`.adaptive.ui.builtin.signOut
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.WsSideBarAction
import `fun`.adaptive.ui.workspace.model.WsPanePosition

fun Workspace.appSignOutAction() {

    sideBarActions += WsSideBarAction(
        Strings.signOut,
        Graphics.power_settings_new,
        WsPanePosition.LeftBottom,
        Int.MAX_VALUE,
        null
    ) { }

}