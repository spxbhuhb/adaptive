package `fun`.adaptive.iot.space.ui.browser

import `fun`.adaptive.iot.space.AioSpaceApi
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.WithWorkspace
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.ui.workspace.model.WsItem

class SpaceBrowserContentController(
    override val workspace: Workspace
) : WsPaneController<SpaceBrowserWsItem>(), WithWorkspace {

    val spaceService = getService<AioSpaceApi>(transport)

    override fun accepts(pane: WsPaneType<SpaceBrowserWsItem>, modifiers: Set<EventModifier>, item: WsItem): Boolean {
        return (item is SpaceBrowserWsItem)
    }

    override fun load(pane: WsPaneType<SpaceBrowserWsItem>, modifiers: Set<EventModifier>, item: WsItem): WsPaneType<SpaceBrowserWsItem> {
        return pane.copy(
            name = item.name,
            data = item as SpaceBrowserWsItem,
            icon = item.config.icon
        )
    }

}