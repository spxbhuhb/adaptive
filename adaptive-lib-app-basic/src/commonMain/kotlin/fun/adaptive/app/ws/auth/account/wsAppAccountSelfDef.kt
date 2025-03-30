package `fun`.adaptive.app.ws.auth.account

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.accountSelf
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.builtin.account
import `fun`.adaptive.ui.builtin.account_circle
import `fun`.adaptive.ui.builtin.home
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.WsSideBarAction
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

const val ACCOUNT_SELF_KEY: FragmentKey = "app:ws:account:self"
val ACCOUNT_SELF_ITEM by lazy { SingularWsItem(Strings.home, ACCOUNT_SELF_KEY) }

fun Workspace.wsAppAccountSelfDef() {

    addContentPaneBuilder(ACCOUNT_SELF_KEY) {
        WsPane(
            UUID(),
            Strings.accountSelf,
            Graphics.account_circle,
            WsPanePosition.Center,
            ACCOUNT_SELF_KEY,
            ACCOUNT_SELF_ITEM,
            AccountSelfController(this),
            displayOrder = Int.MAX_VALUE - 1
        )
    }

    + WsSideBarAction(
        Strings.account,
        Graphics.account_circle,
        WsPanePosition.LeftBottom,
        displayOrder = Int.MAX_VALUE - 1,
        null
    ) {
        addContent(ACCOUNT_SELF_ITEM)
    }

}