package `fun`.adaptive.iot.space.ui.browser

import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType

class SpaceBrowserContentController(
    override val workspace: Workspace
) : WsPaneController<SpaceBrowserWsItem>() {

    override fun accepts(pane: WsPaneType<SpaceBrowserWsItem>, modifiers: Set<EventModifier>, item: NamedItem): Boolean {
        return (item is SpaceBrowserWsItem)
    }

    override fun load(pane: WsPaneType<SpaceBrowserWsItem>, modifiers: Set<EventModifier>, item: NamedItem): WsPaneType<SpaceBrowserWsItem> {
        return pane.copy(
            name = item.name,
            data = item as SpaceBrowserWsItem,
            icon = item.config.icon
        )
    }

}