package `fun`.adaptive.app.ws.auth.admin

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.accounts
import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.supervised_user_circle
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController
import `fun`.adaptive.ui.workspace.model.SingularWsItem

class AccountManagerController : WsSingularPaneController(ACCOUNT_MANAGER_ITEM) {

    override val adminTool: Boolean
        get() = true

}