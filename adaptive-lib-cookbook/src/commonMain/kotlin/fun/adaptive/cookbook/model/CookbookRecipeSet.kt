package `fun`.adaptive.cookbook.model

import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.builtin.folder
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem

class CookbookRecipeSet(
    val name: String,
    val subsets : List<CookbookRecipeSet>,
    val recipes: List<WsRecipeItem>
) {
    fun toTreeItem(onClick : (TreeItem, Set<EventModifier>) -> Unit) : TreeItem =
        TreeItem(
            Graphics.folder,
            title = name,
            children = subsets.map { it.toTreeItem(onClick) } + recipes.map { it.toTreeItem(onClick) },
            onClick = onClick
        )

}