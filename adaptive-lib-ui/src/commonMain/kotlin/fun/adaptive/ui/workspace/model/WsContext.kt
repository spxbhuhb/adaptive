package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.ui.workspace.Workspace

interface WsContext {

    val workspace: Workspace

    fun pane(key: String) =
        workspace.toolPanes.first { it.key == key }

    operator fun get(item : WsItem) = workspace.getItemConfig(item.type)

}