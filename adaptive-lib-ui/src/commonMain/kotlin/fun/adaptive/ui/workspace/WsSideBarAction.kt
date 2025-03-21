package `fun`.adaptive.ui.workspace

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.workspace.Workspace.Companion.noContentPane
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.model.WsPaneSingularity

class WsSideBarAction(
    override val name: String,
    override val icon: GraphicsResourceSet,
    override val position: WsPanePosition,
    override val displayOrder: Int,
    override val tooltip: String?,
    override val actionFun: (Workspace) -> Unit
) : AbstractSideBarAction()