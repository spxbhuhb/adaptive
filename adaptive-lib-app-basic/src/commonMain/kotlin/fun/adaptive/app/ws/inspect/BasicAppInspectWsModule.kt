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

class BasicAppInspectWsModule<AT : Any> : AppModule<Workspace, AT>() {

    companion object {
        const val INSPECT_TOOL_KEY: FragmentKey = "app:ws:inspect:tool"
    }

    override fun AdaptiveAdapter.init() {
        fragmentFactory += WsAppInspectFragmentFactory
    }

    override fun Workspace.init() {
        wsAppInspectToolDef()
    }

    fun Workspace.wsAppInspectToolDef() {

        + WsPane(
            UUID(),
            Strings.devTools,
            Graphics.pest_control,
            WsPanePosition.RightBottom,
            INSPECT_TOOL_KEY,
            Unit,
            WsUnitPaneController()
        )

    }

}