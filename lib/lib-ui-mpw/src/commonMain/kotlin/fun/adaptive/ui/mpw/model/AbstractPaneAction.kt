package `fun`.adaptive.ui.mpw.model

import `fun`.adaptive.resource.graphics.GraphicsResourceSet

abstract class AbstractPaneAction(
    val icon: GraphicsResourceSet,
    val tooltip: String
) {
    abstract fun execute()
}