package `fun`.adaptive.ui.mpw

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.mpw.model.PanePosition

class SideBarAction(
    override val name: String,
    override val icon: GraphicsResourceSet,
    override val position: PanePosition,
    override val displayOrder: Int,
    override val tooltip: String?,
    override val actionFun: (MultiPaneWorkspace) -> Unit
) : AbstractSideBarAction()