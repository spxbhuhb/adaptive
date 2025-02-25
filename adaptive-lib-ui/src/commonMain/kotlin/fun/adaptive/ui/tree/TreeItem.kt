package `fun`.adaptive.ui.tree

import `fun`.adaptive.resource.graphics.GraphicsResourceSet

open class TreeItem(
    val icon: GraphicsResourceSet,
    val title: String,
    val children: List<TreeItem>,
    val onClick: (TreeItem) -> Unit = { },
    val data: Any? = null
)