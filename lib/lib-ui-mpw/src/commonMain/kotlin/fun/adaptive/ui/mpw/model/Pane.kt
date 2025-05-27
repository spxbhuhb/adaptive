package `fun`.adaptive.ui.mpw.model

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.mpw.AbstractSideBarAction
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend

data class Pane<VB : PaneViewBackend<VB>>(
    val uuid: PaneId,
    override val workspace: MultiPaneWorkspace,
    override val name: String,
    override val icon: GraphicsResourceSet,
    override val position: PanePosition,
    val key: FragmentKey,
    val viewBackend: VB,
    override val tooltip: String? = null,
    val actions: List<AbstractPaneAction<*>> = emptyList(),
    override val singularity: PaneSingularity = PaneSingularity.GROUP,
    override val displayOrder: Int = Int.MAX_VALUE
) : AbstractSideBarAction() {

    fun accepts(item: WsPaneItem, modifier: Set<EventModifier>) =
        viewBackend.accepts(this, modifier, item)

    fun load(item: WsPaneItem, modifier: Set<EventModifier>) =
        viewBackend.load(this, modifier, item)

    override val pane: Pane<VB>
        get() = this

    override val actionFun: (MultiPaneWorkspace) -> Unit = { it.toggle(this) }

}