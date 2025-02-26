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
fun cookbookRecipes(): AdaptiveFragment {
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
            "Demos",
            emptyList(),
            listOf(
                CookbookRecipe("Good Morning", "cookbook:recipe:goodmorning")
            )
        ),
        CookbookRecipeSet(
            "UI Fragments",
            emptyList(),
            listOf(
                CookbookRecipe("Box", "cookbook:recipe:box"),
                CookbookRecipe("Button", "cookbook:recipe:button"),
                CookbookRecipe("Canvas", "cookbook:recipe:canvas"),
                CookbookRecipe("Dialog", "cookbook:recipe:dialog"),
                CookbookRecipe("Editor", "cookbook:recipe:editor"),
                CookbookRecipe("Event", "cookbook:recipe:event"),
                CookbookRecipe("Form", "cookbook:recipe:form"),
                CookbookRecipe("Grid", "cookbook:recipe:grid"),
                CookbookRecipe("Popup", "cookbook:recipe:popup"),
                CookbookRecipe("Select", "cookbook:recipe:select"),
                CookbookRecipe("Sidebar", "cookbook:recipe:sidebar"),
                CookbookRecipe("Snackbar", "cookbook:recipe:snackbar"),
                CookbookRecipe("Split Pane", "cookbook:recipe:splitpane"),
                CookbookRecipe("SVG", "cookbook:recipe:svg"),
                CookbookRecipe("Text", "cookbook:recipe:text"),
                CookbookRecipe("Tree", "cookbook:recipe:tree")
            )
        )
    ),
    emptyList()
)

