package `fun`.adaptive.iot.space.ui

import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.iot.space.markers.SpaceMarkers
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.ui.workspace.model.WsItem

class SpaceContentController(context: AioWsContext) : WsPaneController<WsItem>() {

    override fun accepts(pane: WsPaneType<WsItem>, modifiers: Set<EventModifier>, item: WsItem): Boolean {
        return (item is AioItem) && (SpaceMarkers.SPACE in item.markers)
    }

    override fun load(pane: WsPaneType<WsItem>, modifiers: Set<EventModifier>, item: WsItem): WsPaneType<WsItem> {
        return pane
    }
}