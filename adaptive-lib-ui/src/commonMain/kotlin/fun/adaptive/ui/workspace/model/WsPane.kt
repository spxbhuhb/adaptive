package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType

data class WsPane<D, C : WsPaneController<D>>(
    val uuid: WsPaneId,
    val name: String,
    val icon: GraphicsResourceSet,
    val position: WsPanePosition,
    val key : FragmentKey,
    val data: D,
    val controller: C,
    val tooltip: String? = null,
    val actions : List<AbstractWsPaneAction<*>> = emptyList(),
    val singularity : WsPaneSingularity = WsPaneSingularity.GROUP,
) {

    @Suppress("UNCHECKED_CAST")
    fun accepts(item : WsItem, modifier: Set<EventModifier>) =
        controller.accepts(this as WsPaneType<D>, modifier, item)

    @Suppress("UNCHECKED_CAST")
    fun load(item : WsItem, modifier: Set<EventModifier>) =
        controller.load(this as WsPaneType<D>, modifier, item)

}