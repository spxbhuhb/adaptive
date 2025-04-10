package `fun`.adaptive.document.ws.browser

import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType

class DocBrowserContentController(
    override val workspace: Workspace
) : WsPaneController<DocBrowserWsItem>() {

    override fun accepts(pane: WsPaneType<DocBrowserWsItem>, modifiers: Set<EventModifier>, item: NamedItem): Boolean {
        return (item is DocBrowserWsItem)
    }

    override fun load(pane: WsPaneType<DocBrowserWsItem>, modifiers: Set<EventModifier>, item: NamedItem): WsPaneType<DocBrowserWsItem> {
        return pane.copy(
            name = item.name,
            data = item as DocBrowserWsItem,
            icon = item.config.icon
        )
    }

}