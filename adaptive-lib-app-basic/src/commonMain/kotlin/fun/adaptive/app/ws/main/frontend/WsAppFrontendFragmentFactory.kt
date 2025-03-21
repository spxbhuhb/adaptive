package `fun`.adaptive.app.ws.main.frontend

import `fun`.adaptive.app.ws.WsAppModule
import `fun`.adaptive.app.ws.wsAppAccountTool
import `fun`.adaptive.app.ws.wsAppAdminTool
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object WsAppFrontendFragmentFactory : FoundationFragmentFactory() {
    init {
        with(WsAppModule) {
            add(ADMIN_TOOL_KEY, ::wsAppAdminTool)
            add(ACCOUNT_TOOL_KEY, ::wsAppAccountTool)
            add(FRONTEND_MAIN_KEY, ::wsAppFrontendMain)
        }
    }
}