package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.AbstractSideBarAction
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.logic.WsPaneViewBackend

data class WsPane<VB : WsPaneViewBackend<VB>>(
    val uuid: WsPaneId,
    override val workspace: MultiPaneWorkspace,
    override val name: String,
    override val icon: GraphicsResourceSet,
    override val position: WsPanePosition,
    val key: FragmentKey,
    val viewBackend: VB,
    override val tooltip: String? = null,
    val actions: List<AbstractWsPaneAction<*>> = emptyList(),
    override val singularity: WsPaneSingularity = WsPaneSingularity.GROUP,
    override val displayOrder: Int = Int.MAX_VALUE
) : AbstractSideBarAction() {

    fun accepts(item: WsPaneItem, modifier: Set<EventModifier>) =
        viewBackend.accepts(this, modifier, item)

    fun load(item: WsPaneItem, modifier: Set<EventModifier>) =
        viewBackend.load(this, modifier, item)

    override val pane: WsPane<VB>
        get() = this

    override val actionFun: (MultiPaneWorkspace) -> Unit = { it.toggle(this) }

}