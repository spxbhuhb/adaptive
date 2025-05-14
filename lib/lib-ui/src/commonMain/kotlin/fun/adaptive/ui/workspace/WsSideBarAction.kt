package `fun`.adaptive.ui.workspace

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.workspace.model.WsPanePosition

class WsSideBarAction(
    override val workspace: MultiPaneWorkspace,
    override val name: String,
    override val icon: GraphicsResourceSet,
    override val position: WsPanePosition,
    override val displayOrder: Int,
    override val tooltip: String?,
    override val actionFun: (MultiPaneWorkspace) -> Unit
) : AbstractSideBarAction()