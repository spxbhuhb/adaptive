package `fun`.adaptive.app.ws.admin

import `fun`.adaptive.auth.app.AuthBasicClientModule
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.ui.workspace.Workspace

class AppAdminWsModule<WT : Workspace> : AuthBasicClientModule<WT>() {

    val ADMIN_TOOL_KEY: String = "app:ws:admin:tool"

    override fun contextInit() {
        application.workspace.contexts += WsAdminContext()
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) {
        adapter.fragmentFactory.add(ADMIN_TOOL_KEY, ::wsAppAdminTool)
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        wsAppAdminToolDef(this@AppAdminWsModule)
    }

}