package `fun`.adaptive.app.ws.auth.account

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.accountSelf
import `fun`.adaptive.app.ws.auth.AppAuthWsModule
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.builtin.account
import `fun`.adaptive.ui.builtin.account_circle
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.WsSideBarAction
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun Workspace.wsAppAccountSelfDef(
    module: AppAuthWsModule<*>
) {

    addContentPaneBuilder(module.ACCOUNT_SELF_KEY) {
        WsPane(
            UUID(),
            workspace = this,
            Strings.accountSelf,
            Graphics.account_circle,
            WsPanePosition.Center,
            module.ACCOUNT_SELF_KEY,
            module.ACCOUNT_SELF_ITEM,
            AccountSelfController(this, module.ACCOUNT_SELF_ITEM),
            displayOrder = Int.MAX_VALUE - 1
        )
    }

    + WsSideBarAction(
        workspace = this,
        Strings.account,
        Graphics.account_circle,
        WsPanePosition.LeftBottom,
        displayOrder = Int.MAX_VALUE - 1,
        null
    ) {
        addContent(module.ACCOUNT_SELF_ITEM)
    }

}