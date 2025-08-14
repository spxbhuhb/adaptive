package `fun`.adaptive.ui.mpw

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.utility.UUID

class SideBarAction(
    override val name : String,
    override val icon : GraphicsResourceSet,
    override val position : PanePosition,
    override val displayOrder : Int,
    override val tooltip : String?,
    override val requiredRole : UUID<*>? = null,
    override val actionFun : (MultiPaneWorkspace) -> Unit
) : AbstractSideBarAction()