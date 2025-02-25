package `fun`.adaptive.ui.workspace

interface WorkspaceContext {

    val workspace: Workspace

    fun pane(key: String) =
        workspace.panes.first { it.key == key }

}