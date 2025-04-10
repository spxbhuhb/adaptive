package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.ui.workspace.Workspace

class WsPaneActionArguments<T>(
    val workspace: Workspace,
    val pane: WsPane<*, *>,
    val data: T
)