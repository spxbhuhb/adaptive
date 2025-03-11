package `fun`.adaptive.ui.workspace.logic

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsItem

class WsUnitPaneController<D> : WsPaneController<D>() {

    override fun accepts(pane : WsPane<D>, modifiers: Set<EventModifier>, item : WsItem) = false

    override fun load(pane : WsPane<D>, modifiers: Set<EventModifier>, item : WsItem) = pane

}