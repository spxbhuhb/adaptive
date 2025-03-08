package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.resource.graphics.GraphicsResourceSet

class WorkspacePaneAction(
    val icon : GraphicsResourceSet,
    val tooltip : String,
    val action : (workspace : Workspace, pane : WorkspacePane) -> Unit
)