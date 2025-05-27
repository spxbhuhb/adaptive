package `fun`.adaptive.grove.ufd.logic

import `fun`.adaptive.grove.apm.model.ApmWsItem
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.model.Pane
import `fun`.adaptive.ui.mpw.model.WsPaneItem

class UfdContentViewBackend(
    override val workspace: MultiPaneWorkspace
) : PaneViewBackend<UfdContentViewBackend>() {

    override fun accepts(pane: Pane<UfdContentViewBackend>, modifiers: Set<EventModifier>, item: WsPaneItem): Boolean =
        false

    override fun load(pane: Pane<UfdContentViewBackend>, modifiers: Set<EventModifier>, item: WsPaneItem): Pane<UfdContentViewBackend> {
        check(item is ApmWsItem)
        return pane
    }

}