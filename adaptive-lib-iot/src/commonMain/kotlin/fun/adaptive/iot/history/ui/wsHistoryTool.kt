package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun wsHistoryTool(pane : WsPane<*, HistoryToolController>) : AdaptiveFragment {

    val names = valueFrom { pane.controller.hisNames }

    wsToolPane(pane) {
        column {
            for (name in names) {
                text(name.names.joinToString("."))
            }
        }
    }

    return fragment()
}