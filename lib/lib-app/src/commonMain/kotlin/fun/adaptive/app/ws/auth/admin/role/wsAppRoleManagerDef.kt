package `fun`.adaptive.app.ws.auth.admin.role

import `fun`.adaptive.lib_app.generated.resources.military_tech
import `fun`.adaptive.lib_app.generated.resources.roles
import `fun`.adaptive.app.ws.addAdminItem
import `fun`.adaptive.app.ws.auth.AppAuthWsModule
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.Pane
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.utility.UUID

fun MultiPaneWorkspace.wsAppRoleManagerDef(
    module: AppAuthWsModule<*>
) {

    addAdminItem(module.ROLE_MANAGER_ITEM)

    addContentPaneBuilder(module.ROLE_MANAGER_KEY) {
        Pane(
            UUID(),
            workspace = this,
            Strings.roles,
            Graphics.military_tech,
            PanePosition.Center,
            module.ROLE_MANAGER_KEY,
            RoleManagerViewBackend(module)
        )
    }

}
