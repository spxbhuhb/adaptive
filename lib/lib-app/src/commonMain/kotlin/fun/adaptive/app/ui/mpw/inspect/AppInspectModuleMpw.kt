package `fun`.adaptive.app.ui.mpw.inspect

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.auth.model.AuthMarkers
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.lib_app.generated.resources.devTools
import `fun`.adaptive.lib_app.generated.resources.pest_control
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.utility.UUID

class AppInspectModuleMpw<FW : MultiPaneWorkspace, BW : AbstractWorkspace> : AppModule<FW, BW>() {

    val INSPECT_TOOL_KEY: FragmentKey
        get() = "app:ws:inspect:tool"

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(INSPECT_TOOL_KEY, ::appInspectTool)
    }

    override fun frontendWorkspaceInit(workspace: FW, session: Any?) = with(workspace) {

        val app = application as ClientApplication

        addToolPane {
            UnitPaneViewBackend(
                this,
                PaneDef(
                    UUID(),
                    Strings.devTools,
                    Graphics.pest_control,
                    PanePosition.RightBottom,
                    INSPECT_TOOL_KEY,
                    requiredRole = app.allApplicationRoles.firstOrNull { AuthMarkers.SECURITY_OFFICER in it.markers }?.uuid
                )
            )
        }

    }

}