package `fun`.adaptive.app.ui.mpw.admin

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.app.ui.util.withRole
import `fun`.adaptive.auth.app.AuthBasicClientModule
import `fun`.adaptive.auth.model.AuthMarkers
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.lib_app.generated.resources.administration
import `fun`.adaptive.lib_app.generated.resources.local_police
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.ui.mpw.model.SingularPaneItem
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValueId

class AppAdminModuleMpw<FW : MultiPaneWorkspace, BW : BackendWorkspace> : AuthBasicClientModule<FW, BW>() {

    val ADMIN_TOOL_KEY: String = "app:mpw:admin:tool"

    val plugins = mutableListOf<SingularPaneItem>()
    var securityOfficer: AvValueId? = null

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(ADMIN_TOOL_KEY, ::adminTool)
    }

    override fun contextInit() {
        val app = application as ClientApplication
        securityOfficer = app.knownRoles.firstOrNull { AuthMarkers.SECURITY_OFFICER in it.markers }?.uuid
    }

    override fun frontendWorkspaceInit(workspace: FW, session: Any?) = with(workspace) {
        application.withRole(securityOfficer) {

            val appAdminToolPane = PaneDef(
                UUID(),
                Strings.administration,
                Graphics.local_police,
                PanePosition.RightMiddle,
                ADMIN_TOOL_KEY,
                displayOrder = Int.MAX_VALUE
            )

            addToolPane {
                UnitPaneViewBackend(this, appAdminToolPane)
            }

        }
    }

}