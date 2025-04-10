package `fun`.adaptive.app.ws.auth.admin.role

import `fun`.adaptive.adaptive_lib_app.generated.resources.military_tech
import `fun`.adaptive.adaptive_lib_app.generated.resources.roles
import `fun`.adaptive.app.ws.addAdminItem
import `fun`.adaptive.app.ws.auth.AppAuthWsModule
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun Workspace.wsAppRoleManagerDef(
    module: AppAuthWsModule<*>
) {

    addAdminItem(module.ROLE_MANAGER_ITEM)

    addContentPaneBuilder(module.ROLE_MANAGER_KEY) {
        WsPane(
            UUID(),
            workspace = this,
            Strings.roles,
            Graphics.military_tech,
            WsPanePosition.Center,
            module.ROLE_MANAGER_KEY,
            module.ROLE_MANAGER_ITEM,
            RoleManagerController(module)
        )
    }

}
