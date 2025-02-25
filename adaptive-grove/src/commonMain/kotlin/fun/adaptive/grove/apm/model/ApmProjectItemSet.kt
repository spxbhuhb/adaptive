package `fun`.adaptive.grove.apm.model

import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.builtin.account_circle
import `fun`.adaptive.ui.tree.TreeItem

class ApmProjectItemSet(
    val name: String,
    val subsets: List<ApmProjectItemSet>,
    val recipes: List<ApmProjectItem>
) {
    fun toTreeItem(onClick: (TreeItem) -> Unit): TreeItem =
        TreeItem(
            Graphics.account_circle,
            title = "",
            children = subsets.map { it.toTreeItem(onClick) } + recipes.map { it.toTreeItem(onClick) },
            onClick = onClick
        )

}