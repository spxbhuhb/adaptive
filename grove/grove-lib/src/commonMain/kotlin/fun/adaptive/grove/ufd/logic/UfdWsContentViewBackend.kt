package `fun`.adaptive.grove.ufd.logic

import `fun`.adaptive.grove.apm.model.ApmWsItem
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.logic.WsPaneViewBackend
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneItem

class UfdWsContentViewBackend(
    override val workspace: MultiPaneWorkspace
) : WsPaneViewBackend<UfdWsContentViewBackend>() {

    override fun accepts(pane: WsPane<UfdWsContentViewBackend>, modifiers: Set<EventModifier>, item: WsPaneItem): Boolean =
        false

    override fun load(pane: WsPane<UfdWsContentViewBackend>, modifiers: Set<EventModifier>, item: WsPaneItem): WsPane<UfdWsContentViewBackend> {
        check(item is ApmWsItem)
        return pane
    }

}