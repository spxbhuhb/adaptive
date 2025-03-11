package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneController

data class WsPane<D>(
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

    fun accepts(item : WsItem, modifier: Set<EventModifier>) =
        controller.accepts(this, modifier, item)

    fun load(item : WsItem, modifier: Set<EventModifier>) =
        controller.load(this, modifier, item)

}