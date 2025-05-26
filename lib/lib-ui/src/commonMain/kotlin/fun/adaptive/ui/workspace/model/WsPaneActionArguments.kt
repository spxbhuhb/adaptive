package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.ui.workspace.MultiPaneWorkspace

class WsPaneActionArguments<T>(
    val workspace: MultiPaneWorkspace,
    val pane: WsPane<*>,
    val data: T
)