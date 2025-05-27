package `fun`.adaptive.ui.mpw.backends

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.Pane
import `fun`.adaptive.ui.mpw.model.WsPaneItem

class UnitPaneViewBackend(
    override val workspace: MultiPaneWorkspace
) : PaneViewBackend<UnitPaneViewBackend>() {

    override fun accepts(pane: Pane<UnitPaneViewBackend>, modifiers: Set<EventModifier>, item: WsPaneItem) = false

    override fun load(pane: Pane<UnitPaneViewBackend>, modifiers: Set<EventModifier>, item: WsPaneItem) = pane.copy()

}