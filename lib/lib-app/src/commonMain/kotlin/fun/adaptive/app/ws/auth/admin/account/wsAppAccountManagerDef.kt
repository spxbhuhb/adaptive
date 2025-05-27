package `fun`.adaptive.app.ws.auth.admin.account

import `fun`.adaptive.lib_app.generated.resources.accounts
import `fun`.adaptive.lib_app.generated.resources.supervised_user_circle
import `fun`.adaptive.app.ws.addAdminItem
import `fun`.adaptive.app.ws.auth.AppAuthWsModule
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.Pane
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.utility.UUID

fun MultiPaneWorkspace.wsAppAccountManagerDef(
    module: AppAuthWsModule<*>
) {

    addAdminItem(module.ACCOUNT_MANAGER_ITEM)

    addContentPaneBuilder(module.ACCOUNT_MANAGER_KEY) {
        Pane(
            UUID(),
            workspace = this,
            Strings.accounts,
            Graphics.supervised_user_circle,
            PanePosition.Center,
            module.ACCOUNT_MANAGER_KEY,
            AccountManagerViewBackend(module)
        )
    }

}
