package `fun`.adaptive.app.ws.inspect

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.devTools
import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.pest_control
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneController
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

class BasicAppInspectWsModule<WT : Workspace> : AppModule<WT>() {

    companion object {
        const val INSPECT_TOOL_KEY: FragmentKey = "app:ws:inspect:tool"
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter) {
        + WsAppInspectFragmentFactory
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        wsAppInspectToolDef()
    }

    fun Workspace.wsAppInspectToolDef() {

        + WsPane(
            UUID(),
            workspace = this,
            Strings.devTools,
            Graphics.pest_control,
            WsPanePosition.RightBottom,
            INSPECT_TOOL_KEY,
            Unit,
            WsUnitPaneController(this)
        )

    }

}