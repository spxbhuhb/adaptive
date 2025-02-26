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
        set(
            "Demos",
            CookbookRecipe("Good Morning", "cookbook:recipe:goodmorning")
        ),

        set(
            "Layout",
            CookbookRecipe("Box", "cookbook:recipe:box"),
            CookbookRecipe("Grid", "cookbook:recipe:grid"),
            CookbookRecipe("Split Pane", "cookbook:recipe:splitpane"),
            CookbookRecipe("Workspace", "cookbook:recipe:workspace")
        ),

        set(
            "Basic fragments",
            CookbookRecipe("Button", "cookbook:recipe:button"),
            CookbookRecipe("Icon", "cookbook:recipe:icon"),
            CookbookRecipe("SVG", "cookbook:recipe:svg"),
            CookbookRecipe("Text", "cookbook:recipe:text"),
        ),

        set(
            "Standalone inputs",
            CookbookRecipe("Checkbox", "cookbook:recipe:checkbox"),
            CookbookRecipe("Date picker", "cookbook:recipe:datepicker"),
            CookbookRecipe("Select", "cookbook:recipe:select"),
        ),

        set(
            "Bound inputs - editors",
            CookbookRecipe("Editor", "cookbook:recipe:editor"),
            CookbookRecipe("Form", "cookbook:recipe:form")
        ),

        set(
            "Complex fragments",
            CookbookRecipe("Canvas", "cookbook:recipe:canvas"),
            CookbookRecipe("Dialog", "cookbook:recipe:dialog"),
            CookbookRecipe("Paragraph", "cookbook:recipe:paragraph"),
            CookbookRecipe("Sidebar", "cookbook:recipe:sidebar"),
            CookbookRecipe("Snackbar", "cookbook:recipe:snackbar"),
            CookbookRecipe("Tree", "cookbook:recipe:tree"),
        ),

        set(
            "Other fragments",
            CookbookRecipe("Event", "cookbook:recipe:event"),
            CookbookRecipe("Popup", "cookbook:recipe:popup")
        )
    ),
    emptyList()
)

private fun set(name: String, vararg recipes: CookbookRecipe) =
    CookbookRecipeSet(
        name,
        emptyList(),
        recipes.toList()
    )

