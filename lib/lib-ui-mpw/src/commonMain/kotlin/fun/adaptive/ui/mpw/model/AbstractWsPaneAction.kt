package `fun`.adaptive.ui.mpw.model

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace

abstract class AbstractWsPaneAction<T>(
    val icon: GraphicsResourceSet,
    val tooltip: String
) {
    abstract val data: T
    abstract fun execute(workspace: MultiPaneWorkspace, pane: Pane<*>)
}