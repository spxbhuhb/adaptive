package `fun`.adaptive.app.ws.inspect

import `fun`.adaptive.lib_app.generated.resources.devTools
import `fun`.adaptive.lib_app.generated.resources.pest_control
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneController
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID


fun MultiPaneWorkspace.wsAppInspectToolDef(module: AppInspectWsModule<*>) {

    + WsPane(
        UUID(),
        workspace = this,
        Strings.devTools,
        Graphics.pest_control,
        WsPanePosition.RightBottom,
        module.INSPECT_TOOL_KEY,
        Unit,
        WsUnitPaneController(this)
    )

}