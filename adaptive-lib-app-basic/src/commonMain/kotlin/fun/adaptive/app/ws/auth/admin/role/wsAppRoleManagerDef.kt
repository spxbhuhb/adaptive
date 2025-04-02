package `fun`.adaptive.app.ws.auth.admin.role

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.accounts
import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.military_tech
import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.roles
import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.supervised_user_circle
import `fun`.adaptive.app.ws.admin.WsAdminContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.firstInstance

const val ROLE_MANAGER_KEY = "app:ws:admin:roles"

val ROLE_MANAGER_ITEM by lazy { SingularWsItem(Strings.roles, ROLE_MANAGER_KEY, Graphics.military_tech) }

fun Workspace.wsAppRoleManagerDef() {

    contexts.firstInstance<WsAdminContext>().adminItems += ROLE_MANAGER_ITEM

    addContentPaneBuilder(ROLE_MANAGER_KEY) {
        WsPane(
            UUID(),
            workspace = this,
            Strings.roles,
            Graphics.military_tech,
            WsPanePosition.Center,
            ROLE_MANAGER_KEY,
            ROLE_MANAGER_ITEM,
            RoleManagerController(this)
        )
    }

}
