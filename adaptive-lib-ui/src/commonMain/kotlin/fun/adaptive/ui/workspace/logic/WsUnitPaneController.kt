package `fun`.adaptive.ui.workspace.logic

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsItem

class WsUnitPaneController : WsPaneController<Unit>() {

    override fun accepts(pane: WsPaneType<Unit>, modifiers: Set<EventModifier>, item: WsItem) = false

    override fun load(pane: WsPaneType<Unit>, modifiers: Set<EventModifier>, item: WsItem) = pane

}