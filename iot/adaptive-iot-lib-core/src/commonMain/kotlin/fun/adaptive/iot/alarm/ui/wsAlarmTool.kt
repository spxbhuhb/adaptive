package `fun`.adaptive.iot.alarm.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.iot.generated.resources.notifications
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
fun wsAlarmTool(pane : WsPane<*,*>) : AdaptiveFragment {

    wsToolPane(pane) {
        todo()
    }

    return fragment()
}

const val WSPANE_ALARM_TOOL: FragmentKey = "aio:history:alarm"

fun Workspace.wsAlarmToolDef() {
    + WsPane(
        UUID(),
        workspace = this,
        Strings.notifications,
        Graphics.notifications,
        WsPanePosition.LeftMiddle,
        WSPANE_ALARM_TOOL,
        Unit,
        WsUnitPaneController(this),
        displayOrder = 1
    )
}