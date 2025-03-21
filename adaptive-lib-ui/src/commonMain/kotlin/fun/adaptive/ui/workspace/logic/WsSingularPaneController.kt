package `fun`.adaptive.ui.workspace.logic

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.ui.workspace.model.WsItem

class WsSingularPaneController(
    val item : SingularWsItem
): WsPaneController<SingularWsItem>() {

    override fun accepts(pane: WsPaneType<SingularWsItem>, modifiers: Set<EventModifier>, item: WsItem) : Boolean {
        return (item === this.item)
    }

    override fun load(pane: WsPaneType<SingularWsItem>, modifiers: Set<EventModifier>, item: WsItem) = pane

}