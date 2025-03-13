package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.model.CbWsRecipeItem
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.builtin.folder
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun cookbookRecipes(): AdaptiveFragment {
    val context = fragment().wsContext<CbWsContext>()

    val treeViewModel = TreeViewModel(
        recipes.map { it.toTreeItem(null) },
        selectedFun = { viewModel, item, modifiers ->
            if (item.data.type == CbWsContext.RECIPE_FOLDER_TYPE) return@TreeViewModel
            showRecipe(context, item, modifiers)
            TreeViewModel.defaultSelectedFun(viewModel, item, modifiers)
        },
        context = Unit,
        openWithSingleClick = true
    )

    wsToolPane(context.pane(CbWsContext.RECIPES_TOOL_KEY)) {
        tree(treeViewModel)
    }

    return fragment()
}

private fun showRecipe(context: CbWsContext, item: TreeItem<CbWsRecipeItem>, modifiers: Set<EventModifier>) {
    context.workspace.addContent(item.data, modifiers)
}

val recipes =
    listOf(
        set(
            "Demos",
            CbWsRecipeItem("Good Morning", key = "cookbook:recipe:goodmorning"),
            CbWsRecipeItem("Markdown", key = "cookbook:recipe:markdown:demo"),
        ),

        set(
            "Layout",
            CbWsRecipeItem("Box", key = "cookbook:recipe:box"),
            CbWsRecipeItem("Grid", key = "cookbook:recipe:grid"),
            CbWsRecipeItem("Split Pane", key = "cookbook:recipe:splitpane"),
            CbWsRecipeItem("Tab", key = "cookbook:recipe:tab"),
            CbWsRecipeItem("Workspace", key = "cookbook:recipe:workspace"),
            CbWsRecipeItem("Wrap", key = "cookbook:recipe:wrap")
        ),

        set(
            "Basic fragments",
            CbWsRecipeItem("Button", key = "cookbook:recipe:button"),
            CbWsRecipeItem("Icon", key = "cookbook:recipe:icon"),
            CbWsRecipeItem("SVG", key = "cookbook:recipe:svg"),
            CbWsRecipeItem("Text", key = "cookbook:recipe:text"),
        ),

        set(
            "Textual fragments",
            CbWsRecipeItem("Code Fence", key = "cookbook:recipe:codefence"),
            CbWsRecipeItem("Paragraph", key = "cookbook:recipe:paragraph"),
            CbWsRecipeItem("Text", key = "cookbook:recipe:text")
        ),

        set(
            "Standalone inputs",
            CbWsRecipeItem("Checkbox", key = "cookbook:recipe:checkbox"),
            CbWsRecipeItem("Date picker", key = "cookbook:recipe:datepicker"),
            CbWsRecipeItem("Select", key = "cookbook:recipe:select"),
        ),

        set(
            "Bound inputs - editors",
            CbWsRecipeItem("Editor", key = "cookbook:recipe:editor"),
            CbWsRecipeItem("Form", key = "cookbook:recipe:form")
        ),

        set(
            "Complex fragments",
            CbWsRecipeItem("Canvas", key = "cookbook:recipe:canvas"),
            CbWsRecipeItem("Dialog", key = "cookbook:recipe:dialog"),
            CbWsRecipeItem("Paragraph", key = "cookbook:recipe:paragraph"),
            CbWsRecipeItem("Sidebar", key = "cookbook:recipe:sidebar"),
            CbWsRecipeItem("Snackbar", key = "cookbook:recipe:snackbar"),
            CbWsRecipeItem("Tree", key = "cookbook:recipe:tree"),
        ),

        set(
            "Other fragments",
            CbWsRecipeItem("Event", key = "cookbook:recipe:event"),
            CbWsRecipeItem("Popup", key = "cookbook:recipe:popup")
        )
    )

private fun set(name: String, vararg recipes: CbWsRecipeItem) =
    CbWsRecipeItem(
        name,
        Graphics.folder,
        CbWsContext.RECIPE_FOLDER_TYPE,
        children = recipes.toList()
    )

