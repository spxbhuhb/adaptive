package `fun`.adaptive.app.ws.inspect

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.lib_app.generated.resources.devTools
import `fun`.adaptive.lib_app.generated.resources.pest_control
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.utility.UUID

class AppInspectWsModule<WT : MultiPaneWorkspace> : AppModule<WT>() {

    val INSPECT_TOOL_KEY: FragmentKey
        get() = "app:ws:inspect:tool"

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(INSPECT_TOOL_KEY, ::wsAppInspectTool)
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {

        addToolPane {
            UnitPaneViewBackend(
                this,
                PaneDef(
                    UUID(),
                    Strings.devTools,
                    Graphics.pest_control,
                    PanePosition.RightBottom,
                    INSPECT_TOOL_KEY,
                )
            )
        }

    }

}