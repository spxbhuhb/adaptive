package `fun`.adaptive.ui.workspace

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.model.WsPaneSingularity

abstract class AbstractSideBarAction {

    abstract val workspace: Workspace

    abstract val name: String
    abstract val icon: GraphicsResourceSet
    abstract val position: WsPanePosition
    abstract val displayOrder: Int
    abstract val tooltip: String?
    abstract val actionFun: (workspace: Workspace) -> Unit

    open val pane: WsPane<*, *>
        get() = workspace.noContentPane

    open val singularity: WsPaneSingularity
        get() = WsPaneSingularity.GROUP
}