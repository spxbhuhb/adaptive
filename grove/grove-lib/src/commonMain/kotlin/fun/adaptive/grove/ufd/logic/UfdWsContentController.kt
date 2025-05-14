package `fun`.adaptive.grove.ufd.logic

import `fun`.adaptive.grove.apm.model.ApmWsItem
import `fun`.adaptive.grove.ufd.model.UfdWsContentPaneData
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace

class UfdWsContentController(
    override val workspace: MultiPaneWorkspace
) : WsPaneController<UfdWsContentPaneData>() {

    override fun accepts(pane: WsPaneType<UfdWsContentPaneData>, modifiers: Set<EventModifier>, item: NamedItem): Boolean =
        false

    override fun load(pane: WsPaneType<UfdWsContentPaneData>, modifiers: Set<EventModifier>, item: NamedItem): WsPaneType<UfdWsContentPaneData> {
        check(item is ApmWsItem)
        return pane
    }

}