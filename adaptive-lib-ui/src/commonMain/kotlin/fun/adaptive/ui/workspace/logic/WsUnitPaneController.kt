package `fun`.adaptive.ui.workspace.logic

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.model.NamedItem

class WsUnitPaneController : WsPaneController<Unit>() {

    override fun accepts(pane: WsPaneType<Unit>, modifiers: Set<EventModifier>, item: NamedItem) = false

    override fun load(pane: WsPaneType<Unit>, modifiers: Set<EventModifier>, item: NamedItem) = pane

}