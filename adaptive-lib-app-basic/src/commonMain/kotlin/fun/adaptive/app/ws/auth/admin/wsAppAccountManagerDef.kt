package `fun`.adaptive.app.ws.auth.admin

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.accounts
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

const val ACCOUNT_MANAGER_KEY = "app:ws:admin:accounts"

val ACCOUNT_MANAGER_ITEM by lazy { SingularWsItem(Strings.accounts, ACCOUNT_MANAGER_KEY, Graphics.supervised_user_circle) }

fun Workspace.wsAppAccountManagerDef() {

    contexts.firstInstance<WsAdminContext>().adminItems += ACCOUNT_MANAGER_ITEM

    addContentPaneBuilder(ACCOUNT_MANAGER_KEY) {
        WsPane(
            UUID(),
            Strings.accounts,
            Graphics.supervised_user_circle,
            WsPanePosition.Center,
            ACCOUNT_MANAGER_KEY,
            ACCOUNT_MANAGER_ITEM,
            AccountManagerController()
        )
    }

}
