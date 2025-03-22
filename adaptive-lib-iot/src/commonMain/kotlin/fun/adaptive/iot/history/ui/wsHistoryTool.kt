package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.historicalData
import `fun`.adaptive.adaptive_lib_iot.generated.resources.monitoring
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.misc.todo
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneController
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.wsToolPane
import `fun`.adaptive.utility.UUID

@Adaptive
fun wsHistoryTool(pane : WsPane<*,*>) : AdaptiveFragment {

    wsToolPane(pane) {
        todo()
    }

    return fragment()
}

const val WSPANE_HISTORY_TOOL: FragmentKey = "aio:history:tool"

fun Workspace.wsHistoryToolDef() {
    + WsPane(
        UUID(),
        Strings.historicalData,
        Graphics.monitoring,
        WsPanePosition.LeftTop,
        WSPANE_HISTORY_TOOL,
        Unit,
        WsUnitPaneController()
    )
}