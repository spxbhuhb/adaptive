package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.workspace.Workspace

class WsPaneAction(
    val icon : GraphicsResourceSet,
    val tooltip : String,
    val action : (workspace : Workspace, pane : WsPane<*>) -> Unit
)