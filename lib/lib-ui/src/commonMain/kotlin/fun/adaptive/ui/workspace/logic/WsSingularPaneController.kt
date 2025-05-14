package `fun`.adaptive.ui.workspace.logic

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace

open class WsSingularPaneController(
    override val workspace: MultiPaneWorkspace,
    val item : SingularWsItem
): WsPaneController<SingularWsItem>() {

    override fun accepts(pane: WsPaneType<SingularWsItem>, modifiers: Set<EventModifier>, item: NamedItem) : Boolean {
        return (item === this.item)
    }

    override fun load(pane: WsPaneType<SingularWsItem>, modifiers: Set<EventModifier>, item: NamedItem) = pane

}