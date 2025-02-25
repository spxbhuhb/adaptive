package `fun`.adaptive.cookbook.model

import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.builtin.account_circle
import `fun`.adaptive.ui.tree.TreeItem

class CookbookRecipeSet(
    val name: String,
    val subsets : List<CookbookRecipeSet>,
    val recipes: List<CookbookRecipe>
) {
    fun toTreeItem(onClick : (TreeItem) -> Unit) : TreeItem =
        TreeItem(
            Graphics.account_circle,
            title = "",
            children = subsets.map { it.toTreeItem(onClick) } + recipes.map { it.toTreeItem(onClick) },
            onClick = onClick
        )

}