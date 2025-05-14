package `fun`.adaptive.ui.workspace.logic

import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace

class WsUnitPaneController(
    override val workspace: MultiPaneWorkspace
) : WsPaneController<Unit>() {

    override fun accepts(pane: WsPaneType<Unit>, modifiers: Set<EventModifier>, item: NamedItem) = false

    override fun load(pane: WsPaneType<Unit>, modifiers: Set<EventModifier>, item: NamedItem) = pane

}