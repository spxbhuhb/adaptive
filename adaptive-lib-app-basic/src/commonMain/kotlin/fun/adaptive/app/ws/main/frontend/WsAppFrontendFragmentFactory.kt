package `fun`.adaptive.app.ws.main.frontend

import `fun`.adaptive.app.ws.BasicAppWsModule
import `fun`.adaptive.app.ws.auth.account.wsAppAccountSelf
import `fun`.adaptive.app.ws.wsAppAdminTool
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object WsAppFrontendFragmentFactory : FoundationFragmentFactory() {
    init {
        with(BasicAppWsModule) {
            add(ADMIN_TOOL_KEY, ::wsAppAdminTool)
            add(ACCOUNT_SELF_KEY, ::wsAppAccountSelf)
            add(FRONTEND_MAIN_KEY, ::wsAppFrontendMain)
        }
    }
}