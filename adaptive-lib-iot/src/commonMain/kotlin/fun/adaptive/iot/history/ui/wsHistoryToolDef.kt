package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.foundation.FragmentKey
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

const val WSPANE_HISTORY_TOOL: FragmentKey = "aio:history:tool"

fun Workspace.wsHistoryToolDef() {
    val controller = HistoryToolController(this)

    + WsPane(
        UUID(),
        Strings.historicalData,
        Graphics.monitoring,
        WsPanePosition.LeftTop,
        WSPANE_HISTORY_TOOL,
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