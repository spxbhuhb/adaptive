package `fun`.adaptive.iot.space.ui

import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.iot.space.AioSpaceApi
import `fun`.adaptive.iot.space.markers.SpaceMarkers
import `fun`.adaptive.iot.ui.iconFor
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.WithWorkspace
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.ui.workspace.model.WsItem

class SpaceContentController(
    override val workspace: Workspace
) : WsPaneController<AioItem>(), WithWorkspace {

    val spaceService = getService<AioSpaceApi>(transport)

    override fun accepts(pane: WsPaneType<AioItem>, modifiers: Set<EventModifier>, item: WsItem): Boolean {
        return (item is AioItem) && (SpaceMarkers.SPACE in item.markers)
    }

    override fun load(pane: WsPaneType<AioItem>, modifiers: Set<EventModifier>, item: WsItem): WsPaneType<AioItem> {
        return pane.copy(
            name = item.name,
            data = item as AioItem,
            icon = iconFor(item)
        )
    }
}