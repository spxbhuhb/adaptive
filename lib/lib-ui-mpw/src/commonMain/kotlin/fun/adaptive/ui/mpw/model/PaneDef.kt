package `fun`.adaptive.ui.mpw.model

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.mpw.AbstractSideBarAction
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend

data class PaneDef(
    val uuid: PaneId,
    override val name: String,
    override val icon: GraphicsResourceSet,
    override val position: PanePosition,
    val key: FragmentKey,
    override val tooltip: String? = null,
    override val singularity: PaneSingularity = PaneSingularity.GROUP,
    override val displayOrder: Int = Int.MAX_VALUE
) : AbstractSideBarAction() {

    override val pane: PaneDef
        get() = this

    override val actionFun: (MultiPaneWorkspace) -> Unit = { it.toggle(this) }

}