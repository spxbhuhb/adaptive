package `fun`.adaptive.ui.mpw.model

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend

open class PaneAction(
    icon: GraphicsResourceSet,
    tooltip: String,
    val action: () -> Unit
) : AbstractPaneAction(icon, tooltip) {

    override fun execute() = action()

}