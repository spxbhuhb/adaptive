package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace

open class WsPaneAction<T>(
    icon : GraphicsResourceSet,
    tooltip : String,
    override val data : T,
    val action : (WsPaneActionArguments<T>) -> Unit
) : AbstractWsPaneAction<T>(icon, tooltip){

    override fun execute(workspace: MultiPaneWorkspace, pane: WsPane<*>) =
        action(WsPaneActionArguments<T>(workspace, pane, data))

}