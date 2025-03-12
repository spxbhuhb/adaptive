package `fun`.adaptive.ui.tree

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.instruction.event.EventModifier

open class TreeItem<T>(
    val icon: GraphicsResourceSet,
    val title: String,
    val children: List<TreeItem<T>>,
    val data: T,
    val onClick: (TreeItem<T>, Set<EventModifier>) -> Unit = { _, _ -> }
)