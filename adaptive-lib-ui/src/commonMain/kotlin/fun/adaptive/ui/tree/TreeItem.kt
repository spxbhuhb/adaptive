package `fun`.adaptive.ui.tree

import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.ui.navigation.NavState

open class TreeItem(
    val icon: DrawableResource,
    val title: String,
    val children: List<TreeItem>,
)