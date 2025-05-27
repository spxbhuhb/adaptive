package `fun`.adaptive.ui.mpw.backends

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.mpw.model.SingularPaneItem
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.Pane
import `fun`.adaptive.ui.mpw.model.WsPaneItem

open class SingularPaneViewBackend<T : SingularPaneViewBackend<T>>(
    override val workspace: MultiPaneWorkspace,
    val item : SingularPaneItem
): PaneViewBackend<T>() {

    override fun accepts(pane: Pane<T>, modifiers: Set<EventModifier>, item: WsPaneItem) : Boolean {
        return (item === this.item)
    }

    override fun load(pane: Pane<T>, modifiers: Set<EventModifier>, item: WsPaneItem) =
        pane.copy()

}