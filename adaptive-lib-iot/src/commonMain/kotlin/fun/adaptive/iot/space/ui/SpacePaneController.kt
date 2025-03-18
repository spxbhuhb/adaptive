package `fun`.adaptive.iot.space.ui

import `fun`.adaptive.iot.space.model.AioSpace
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsPane

class SpacePaneController(context: AioWsContext) : WsPaneController<WsItem>() {

    override fun accepts(pane: WsPane<WsItem>, modifiers: Set<EventModifier>, item: WsItem): Boolean {
        return item is AioSpace
    }

    override fun load(pane: WsPane<WsItem>, modifiers: Set<EventModifier>, item: WsItem): WsPane<WsItem> {
        return pane
    }
}