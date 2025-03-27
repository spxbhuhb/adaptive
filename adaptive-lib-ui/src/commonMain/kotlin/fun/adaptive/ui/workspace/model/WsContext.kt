package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.ui.workspace.Workspace
import kotlinx.coroutines.launch

interface WsContext {

    val workspace: Workspace

    fun pane(key: String) =
        workspace.toolPanes.first { it.key == key }

    operator fun get(item : NamedItem) = workspace.getItemConfig(item.type)

    fun io( ioFun : suspend () -> Unit) {
        workspace.scope.launch { ioFun() }
    }

}