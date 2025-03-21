package `fun`.adaptive.document.ws.browser

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.WithWorkspace
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.ui.workspace.model.WsItem

class DocBrowserContentController(
    override val workspace: Workspace
) : WsPaneController<DocBrowserWsItem>(), WithWorkspace {

    override fun accepts(pane: WsPaneType<DocBrowserWsItem>, modifiers: Set<EventModifier>, item: WsItem): Boolean {
        return (item is DocBrowserWsItem)
    }

    override fun load(pane: WsPaneType<DocBrowserWsItem>, modifiers: Set<EventModifier>, item: WsItem): WsPaneType<DocBrowserWsItem> {
        return pane.copy(
            name = item.name,
            data = item as DocBrowserWsItem,
            icon = item.config.icon
        )
    }

}