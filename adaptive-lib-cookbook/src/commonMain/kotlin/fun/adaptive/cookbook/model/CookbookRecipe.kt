package `fun`.adaptive.cookbook.model

import `fun`.adaptive.cookbook.dining
import `fun`.adaptive.cookbook.flatware
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.builtin.settings
import `fun`.adaptive.ui.tree.TreeItem

class CookbookRecipe(
    val name: String,
    val key: String
) {
    fun toTreeItem(onClick : (TreeItem) -> Unit): TreeItem =
        TreeItem(
            Graphics.dining,
            name,
            emptyList(),
            onClick = onClick,
            data = key
        )
}