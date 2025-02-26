package `fun`.adaptive.ui.workspace

import `fun`.adaptive.resource.graphics.GraphicsResourceSet

class WorkspacePaneAction(
    val icon : GraphicsResourceSet,
    val toolTip : String,
    val action : (workspace : Workspace, pane : WorkspacePane) -> Unit
)