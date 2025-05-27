package `fun`.adaptive.ui.mpw.model

import `fun`.adaptive.ui.mpw.MultiPaneWorkspace

class PaneActionArguments<T>(
    val workspace: MultiPaneWorkspace,
    val pane: Pane<*>,
    val data: T
)