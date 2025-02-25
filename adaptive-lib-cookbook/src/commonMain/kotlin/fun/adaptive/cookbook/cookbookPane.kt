package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.model.CookbookRecipe
import `fun`.adaptive.cookbook.model.CookbookRecipeSet
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun cookbookPane(): AdaptiveFragment {
    val context = fragment().wsContext<CookbookContext>()
    val items = root.toTreeItem { showRecipe(context, it) }.children

    wsToolPane(context.pane(cookbookRecipePaneKey)) {
        tree(items)
    }

    return fragment()
}

private fun showRecipe(context: CookbookContext, item: TreeItem) {
    context.activeRecipeKey.value = (item.data as? String) ?: return

    val ws = context.workspace
    ws.center.value = ws.panes.first { it.key == "cookbook:center" }.uuid
}

val root = CookbookRecipeSet(
    "Root",
    listOf(
        CookbookRecipeSet(
            "UI Fragments",
            emptyList(),
            listOf(
                CookbookRecipe("Snackbar", "cookbook:recipe:snackbar"),
                CookbookRecipe("Split Pane", "cookbook:recipe:splitpane"),
                CookbookRecipe("SVG", "cookbook:recipe:svg"),
                CookbookRecipe("Text", "cookbook:recipe:text"),
                CookbookRecipe("Tree", "cookbook:recipe:tree")
            )
        )
    ),
    listOf(
        CookbookRecipe("Recipe 1", "cookbook:recipe:recipe1"),
        CookbookRecipe("Recipe 2", "cookbook:recipe:recipe2"),
    )
)

