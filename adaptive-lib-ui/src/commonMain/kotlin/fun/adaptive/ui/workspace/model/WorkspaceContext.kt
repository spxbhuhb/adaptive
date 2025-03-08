package `fun`.adaptive.ui.workspace.model

interface WorkspaceContext {

    val workspace: Workspace

    fun pane(key: String) =
        workspace.toolPanes.first { it.key == key }

}