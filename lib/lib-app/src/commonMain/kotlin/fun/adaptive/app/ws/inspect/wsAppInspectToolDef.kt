package `fun`.adaptive.app.ws.inspect

import `fun`.adaptive.lib_app.generated.resources.devTools
import `fun`.adaptive.lib_app.generated.resources.pest_control
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.model.Pane
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.utility.UUID


fun MultiPaneWorkspace.wsAppInspectToolDef(module: AppInspectWsModule<*>) {

    + Pane(
        UUID(),
        workspace = this,
        Strings.devTools,
        Graphics.pest_control,
        PanePosition.RightBottom,
        module.INSPECT_TOOL_KEY,
        UnitPaneViewBackend(this)
    )

}