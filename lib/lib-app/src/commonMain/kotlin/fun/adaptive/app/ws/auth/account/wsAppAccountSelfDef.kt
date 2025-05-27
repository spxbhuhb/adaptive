package `fun`.adaptive.app.ws.auth.account

import `fun`.adaptive.lib_app.generated.resources.accountSelf
import `fun`.adaptive.app.ws.auth.AppAuthWsModule
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.account
import `fun`.adaptive.ui.generated.resources.account_circle
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.SideBarAction
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.utility.UUID

fun MultiPaneWorkspace.wsAppAccountSelfDef(
    module: AppAuthWsModule<*>
) {

    addContentPaneBuilder(module.ACCOUNT_SELF_KEY) {
        PaneDef(
            UUID(),
            workspace = this,
            Strings.accountSelf,
            Graphics.account_circle,
            PanePosition.Center,
            module.ACCOUNT_SELF_KEY,
            AccountSelfViewBackend(this, module.ACCOUNT_SELF_ITEM),
            displayOrder = Int.MAX_VALUE - 1
        )
    }

    + SideBarAction(
        workspace = this,
        Strings.account,
        Graphics.account_circle,
        PanePosition.LeftBottom,
        displayOrder = Int.MAX_VALUE - 1,
        null
    ) {
        addContent(module.ACCOUNT_SELF_ITEM)
    }

}