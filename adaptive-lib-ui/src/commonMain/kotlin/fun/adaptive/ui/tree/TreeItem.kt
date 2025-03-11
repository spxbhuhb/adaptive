package `fun`.adaptive.ui.tree

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.instruction.event.EventModifier

open class TreeItem(
    val icon: GraphicsResourceSet,
    val title: String,
    val children: List<TreeItem>,
    val data: Any? = null,
    val onClick: (TreeItem, Set<EventModifier>) -> Unit = { _, _ -> }
)