package `fun`.adaptive.app.ws.auth.signOut

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.auth.api.AuthSessionApi
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.builtin.power_settings_new
import `fun`.adaptive.ui.builtin.signOut
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.WsSideBarAction
import `fun`.adaptive.ui.workspace.model.WsPanePosition

fun Workspace.wsAppSignOutActionDef(application: AbstractApplication<*>) {

    + WsSideBarAction(
        Strings.signOut,
        Graphics.power_settings_new,
        WsPanePosition.LeftBottom,
        Int.MAX_VALUE,
        null
    ) {
        io {
            getService<AuthSessionApi>(transport).signOut()
            ui {
                (application as? ClientApplication)?.onSignOut()
            }
        }
    }

}