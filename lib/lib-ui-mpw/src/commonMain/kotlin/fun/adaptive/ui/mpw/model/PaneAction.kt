package `fun`.adaptive.ui.mpw.model

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace

open class PaneAction<T>(
    icon : GraphicsResourceSet,
    tooltip : String,
    override val data : T,
    val action : (PaneActionArguments<T>) -> Unit
) : AbstractPaneAction<T>(icon, tooltip){

    override fun execute(workspace: MultiPaneWorkspace, pane: Pane<*>) =
        action(PaneActionArguments<T>(workspace, pane, data))

}