package `fun`.adaptive.ui.workspace.logic

import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsItem

abstract class WsPaneController<D> {

    abstract fun accepts(pane : WsPane<D>, item : WsItem) : Boolean

    abstract fun load(pane : WsPane<D>, item : WsItem)

}