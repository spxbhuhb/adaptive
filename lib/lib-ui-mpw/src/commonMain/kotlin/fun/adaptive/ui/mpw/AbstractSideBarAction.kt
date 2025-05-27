package `fun`.adaptive.ui.mpw

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.mpw.model.Pane
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.ui.mpw.model.PaneSingularity

abstract class AbstractSideBarAction {

    abstract val workspace: MultiPaneWorkspace

    abstract val name: String
    abstract val icon: GraphicsResourceSet
    abstract val position: PanePosition
    abstract val displayOrder: Int
    abstract val tooltip: String?
    abstract val actionFun: (workspace: MultiPaneWorkspace) -> Unit

    open val pane: Pane<*>
        get() = workspace.noContentPane

    open val singularity: PaneSingularity
        get() = PaneSingularity.GROUP
}