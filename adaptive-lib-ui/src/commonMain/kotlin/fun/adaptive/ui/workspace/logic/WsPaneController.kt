package `fun`.adaptive.ui.workspace.logic

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsItem

abstract class WsPaneController<D> {

    open fun accepts(pane: WsPaneType<D>, modifiers: Set<EventModifier>, item: WsItem): Boolean {
        return false
    }

    open fun load(pane: WsPaneType<D>, modifiers: Set<EventModifier>, item: WsItem): WsPaneType<D> {
        throw UnsupportedOperationException("load for pane $pane is not supported")
    }

}