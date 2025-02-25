package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.model.CookbookRecipe
import `fun`.adaptive.cookbook.model.CookbookRecipeSet
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext

@Adaptive
fun cookbookPane() : AdaptiveFragment {
    val context = fragment().wsContext<CookbookContext>()
    val items = root.toTreeItem { showRecipe(context, it) }.children

    tree(items)

    return fragment()
}

private fun showRecipe(context: CookbookContext, item : TreeItem) {
    context.activeRecipeKey.value = (item.data as String)

    val ws = context.workspace
    ws.center.value = ws.panes.first { it.key == "cookbook:center" }.uuid
}

val root = CookbookRecipeSet(
    "Root",
    emptyList(),
    listOf(
        CookbookRecipe("Recipe 1", "cookbook:recipe:recipe1"),
        CookbookRecipe("Recipe 2", "cookbook:recipe:recipe2")
    )
)

