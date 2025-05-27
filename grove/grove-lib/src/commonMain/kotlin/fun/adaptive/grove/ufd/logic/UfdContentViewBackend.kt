package `fun`.adaptive.grove.ufd.logic

import `fun`.adaptive.grove.apm.model.ApmWsItem
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.WsPaneItem

class UfdContentViewBackend(
    override val workspace: MultiPaneWorkspace
) : PaneViewBackend<UfdContentViewBackend>() {

    override fun accepts(pane: PaneDef<UfdContentViewBackend>, modifiers: Set<EventModifier>, item: WsPaneItem): Boolean =
        false

    override fun load(pane: PaneDef<UfdContentViewBackend>, modifiers: Set<EventModifier>, item: WsPaneItem): PaneDef<UfdContentViewBackend> {
        check(item is ApmWsItem)
        return pane
    }

}