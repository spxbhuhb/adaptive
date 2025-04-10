package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.iot.app.IotWsModule
import `fun`.adaptive.iot.generated.resources.*
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.collapseAll
import `fun`.adaptive.ui.generated.resources.expandAll
import `fun`.adaptive.ui.generated.resources.unfold_less
import `fun`.adaptive.ui.generated.resources.unfold_more
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneAction
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun Workspace.wsHistoryToolDef(
    module: IotWsModule<*>
) {
    val controller = HistoryToolController(this)

    + WsPane(
        UUID(),
        workspace = this,
        Strings.historicalData,
        Graphics.monitoring,
        WsPanePosition.LeftTop,
        module.WSPANE_HISTORY_TOOL,
        Unit,
        controller,
        actions = listOf(
            WsPaneAction(Graphics.unfold_more, Strings.expandAll, Unit) { controller.expandAll() },
            WsPaneAction(Graphics.unfold_less, Strings.collapseAll, Unit) { controller.collapseAll() },
            WsPaneAction(Graphics.apartment, Strings.areas, Unit) { controller.showSpaces() },
            WsPaneAction(Graphics.account_tree, Strings.devices, Unit) { controller.showDevices() }
        )
    )

    io {
        controller.start()
    }
}