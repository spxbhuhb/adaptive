package `fun`.adaptive.ui.workspace.logic

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneItem

open class WsSingularPaneViewBackend<T : WsSingularPaneViewBackend<T>>(
    override val workspace: MultiPaneWorkspace,
    val item : SingularWsItem
): WsPaneViewBackend<T>() {

    override fun accepts(pane: WsPane<T>, modifiers: Set<EventModifier>, item: WsPaneItem) : Boolean {
        return (item === this.item)
    }

    override fun load(pane: WsPane<T>, modifiers: Set<EventModifier>, item: WsPaneItem) =
        pane.copy()

}