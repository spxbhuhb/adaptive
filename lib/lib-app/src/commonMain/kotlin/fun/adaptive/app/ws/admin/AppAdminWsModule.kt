package `fun`.adaptive.app.ws.admin

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.app.util.withRole
import `fun`.adaptive.auth.app.AuthBasicClientModule
import `fun`.adaptive.auth.model.AuthMarkers
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.value.AvValueId

class AppAdminWsModule<WT : MultiPaneWorkspace> : AuthBasicClientModule<WT>() {

    val ADMIN_TOOL_KEY: String = "app:ws:admin:tool"

    val adminItems = mutableListOf<SingularWsItem>()
    var securityOfficer : AvValueId? = null

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(ADMIN_TOOL_KEY, ::wsAppAdminTool)
    }

    override fun contextInit() {
        val app = application as ClientApplication
        securityOfficer = app.knownRoles.firstOrNull { AuthMarkers.SECURITY_OFFICER in it.markers }?.uuid
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        application.withRole(securityOfficer) {
            wsAppAdminToolDef(this@AppAdminWsModule)
        }
    }

}