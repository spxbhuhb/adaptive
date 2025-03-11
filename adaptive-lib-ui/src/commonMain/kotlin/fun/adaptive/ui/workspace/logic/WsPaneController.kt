package `fun`.adaptive.ui.workspace.logic

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsItem

abstract class WsPaneController<D> {

    abstract fun accepts(pane : WsPane<D>, modifiers: Set<EventModifier>, item : WsItem) : Boolean

    abstract fun load(pane : WsPane<D>, modifiers: Set<EventModifier>, item : WsItem) : WsPane<D>

}