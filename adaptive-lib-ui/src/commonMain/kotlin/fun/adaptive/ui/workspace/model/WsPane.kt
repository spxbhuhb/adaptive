package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.AbstractSideBarAction
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType

data class WsPane<D, C : WsPaneController<D>>(
    val uuid: WsPaneId,
    override val name: String,
    override val icon: GraphicsResourceSet,
    override val position: WsPanePosition,
    val key: FragmentKey,
    val data: D,
    val controller: C,
    override val tooltip: String? = null,
    val actions: List<AbstractWsPaneAction<*>> = emptyList(),
    override val singularity: WsPaneSingularity = WsPaneSingularity.GROUP,
    override val displayOrder: Int = Int.MAX_VALUE
) : AbstractSideBarAction() {


    @Suppress("UNCHECKED_CAST")
    fun accepts(item: WsItem, modifier: Set<EventModifier>) =
        controller.accepts(this as WsPaneType<D>, modifier, item)

    @Suppress("UNCHECKED_CAST")
    fun load(item: WsItem, modifier: Set<EventModifier>) =
        controller.load(this as WsPaneType<D>, modifier, item)

    override val pane: WsPane<*, *>
        get() = this

    override val actionFun: (Workspace) -> Unit = { it.toggle(this) }

}