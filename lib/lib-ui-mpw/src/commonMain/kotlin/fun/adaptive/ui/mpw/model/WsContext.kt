package `fun`.adaptive.ui.mpw.model

import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import kotlinx.coroutines.launch

interface WsContext {

    val workspace: MultiPaneWorkspace

    fun pane(key: String) =
        workspace.toolPanes.first { it.paneDef.fragmentKey == key }

    operator fun get(item : NamedItem) = workspace.getItemConfig(item.type)

    fun io( ioFun : suspend () -> Unit) {
        workspace.scope.launch { ioFun() }
    }

}