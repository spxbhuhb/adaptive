package `fun`.adaptive.ui.workspace.logic

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneItem

class WsUnitPaneViewBackend(
    override val workspace: MultiPaneWorkspace
) : WsPaneViewBackend<WsUnitPaneViewBackend>() {

    override fun accepts(pane: WsPane<WsUnitPaneViewBackend>, modifiers: Set<EventModifier>, item: WsPaneItem) = false

    override fun load(pane: WsPane<WsUnitPaneViewBackend>, modifiers: Set<EventModifier>, item: WsPaneItem) = pane.copy()

}