package `fun`.adaptive.iot.domain.rht.ui.def

import `fun`.adaptive.iot.app.IotWsModule
import `fun`.adaptive.iot.domain.rht.ui.controller.RhtBrowserToolController
import `fun`.adaptive.iot.generated.resources.dew_point
import `fun`.adaptive.iot.generated.resources.temperatureAndHumidity
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.builtin.collapseAll
import `fun`.adaptive.ui.builtin.expandAll
import `fun`.adaptive.ui.builtin.unfold_less
import `fun`.adaptive.ui.builtin.unfold_more
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneAction
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun Workspace.rhtBrowserToolDef(
    module: IotWsModule<*>
) {

    val workspace = this

    val controller = RhtBrowserToolController(workspace)

    + WsPane(
        UUID(),
        workspace = workspace,
        Strings.temperatureAndHumidity,
        Graphics.dew_point,
        WsPanePosition.LeftTop,
        module.WSPANE_RHT_BROWSER_TOOL,
        actions = listOf(
            WsPaneAction(Graphics.unfold_more, Strings.expandAll, Unit) { controller.expandAll() },
            WsPaneAction(Graphics.unfold_less, Strings.collapseAll, Unit) { controller.collapseAll() },
        ),
        data = Unit,
        controller = controller
    )

    workspace.io {
        controller.start()
    }
}