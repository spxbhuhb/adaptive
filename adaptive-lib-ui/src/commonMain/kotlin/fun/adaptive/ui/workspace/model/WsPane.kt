package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneController

class WsPane<D>(
    val uuid: WsPaneId,
    val name: String,
    val icon: GraphicsResourceSet,
    val position: WsPanePosition,
    val key : FragmentKey,
    val model : D,
    val tooltip: String? = null,
    val actions : List<WsPaneAction> = emptyList(),
    val singularity : WsPaneSingularity = WsPaneSingularity.GROUP,
    val controller : WsPaneController<D> = WsUnitPaneController<D>(),
) {

    fun accepts(item : WsItem) =
        controller.accepts(this, item)

    fun load(item : WsItem) =
        controller.load(this, item)

}