package `fun`.adaptive.app.ws.auth.signOut

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.auth.api.AuthSessionApi
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.generated.resources.power_settings_new
import `fun`.adaptive.ui.generated.resources.signOut
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.SideBarAction
import `fun`.adaptive.ui.mpw.model.PanePosition

fun MultiPaneWorkspace.wsAppSignOutActionDef(application: AbstractApplication<*>) {

    + SideBarAction(
        workspace = this,
        Strings.signOut,
        Graphics.power_settings_new,
        PanePosition.LeftBottom,
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