package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.workspace.Workspace

open class WsPaneAction<T>(
    icon : GraphicsResourceSet,
    tooltip : String,
    override val data : T,
    val action : (workspace : Workspace, pane : WsPane<*>, data : T) -> Unit
) : AbstractWsPaneAction<T>(icon, tooltip){

    override fun execute(workspace: Workspace, pane: WsPane<*>) = action(workspace, pane, data)

}