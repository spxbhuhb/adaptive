package `fun`.adaptive.ui.workspace.logic

import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsItem

class WsUnitPaneController<D> : WsPaneController<D>() {

    override fun accepts(pane : WsPane<D>, item : WsItem) = false

    override fun load(pane : WsPane<D>, item : WsItem) {}

}