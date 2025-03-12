package `fun`.adaptive.grove.ufd.logic

import `fun`.adaptive.grove.apm.model.ApmWsItem
import `fun`.adaptive.grove.ufd.model.UfdWsContentPaneData
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsPane

class UfdWsContentController : WsPaneController<UfdWsContentPaneData>() {

    override fun accepts(pane: WsPane<UfdWsContentPaneData>, modifiers: Set<EventModifier>, item: WsItem): Boolean =
        false

    override fun load(pane: WsPane<UfdWsContentPaneData>, modifiers: Set<EventModifier>, item: WsItem) : WsPane<UfdWsContentPaneData> {
        check(item is ApmWsItem)
        return pane
    }

}