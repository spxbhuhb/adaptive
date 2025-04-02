package `fun`.adaptive.app.ws.auth.admin

import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController

class AccountManagerController(
    workspace : Workspace
) : WsSingularPaneController(workspace, ACCOUNT_MANAGER_ITEM) {

    override val adminTool: Boolean
        get() = true

}