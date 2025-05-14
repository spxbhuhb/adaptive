package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace

abstract class AbstractWsPaneAction<T>(
    val icon: GraphicsResourceSet,
    val tooltip: String
) {
    abstract val data: T
    abstract fun execute(workspace: MultiPaneWorkspace, pane: WsPane<*, *>)
}