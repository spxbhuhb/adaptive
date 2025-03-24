package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.common.AioTheme
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.contextPopup
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.primaryPopup
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun wsHistoryTool(pane : WsPane<*, HistoryToolController>) : AdaptiveFragment {

    val items = valueFrom { pane.controller.hisItems }
    val theme = AioTheme.DEFAULT

    wsToolPane(pane) {
        column {
            for (item in items) {
                box {
                    onClick { pane.controller.openHistory(item.item) }

                    text(item.names.joinToString("."))
                    contextPopup {
                        theme.inlineEditorPopup
                        column {
                            uuidLabel { item.item.uuid }
                        }
                    }
                }
            }
        }
    }

    return fragment()
}