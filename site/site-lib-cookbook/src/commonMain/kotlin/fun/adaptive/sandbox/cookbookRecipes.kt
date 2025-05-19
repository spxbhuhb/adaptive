package `fun`.adaptive.sandbox

import `fun`.adaptive.sandbox.model.CbWsRecipeItem
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewBackend
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun cookbookRecipes(pane: WsPane<*, *>): AdaptiveFragment {
    val context = fragment().wsContext<CbWsContext>()

    val treeViewBackend = TreeViewBackend(
        recipes.map { it.toTreeItem(context, null) },
        selectedFun = { viewModel, item, modifiers ->
            if (item.data.type == CbWsContext.WSIT_CB_RECIPE_FOLDER) return@TreeViewBackend
            showRecipe(context, item, modifiers)
            TreeViewBackend.defaultSelectedFun(viewModel, item, modifiers)
        },
        context = Unit,
        singleClickOpen = true
    )

    wsToolPane(pane) {
        tree(treeViewBackend)
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
        ),

        set(
            "Basic fragments",
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
            "Inputs",
            CbWsRecipeItem("Button", key = "cookbook/input/button/recipe"),
            CbWsRecipeItem("Checkbox", key = "cookbook:recipe:checkbox"),
            CbWsRecipeItem("Date", key = "cookbook:recipe:input:date"),
            CbWsRecipeItem("Double", key = "cookbook:recipe:input:double"),
            CbWsRecipeItem("Quick Filter", key = "cookbook:recipe:input:quick-filter"),
            CbWsRecipeItem("Select", key = "cookbook/input/select/recipe"),
            CbWsRecipeItem("Text input", key = "cookbook/input/text/recipe"),
            CbWsRecipeItem("Text input area", key = "cookbook:recipe:input:text-area")
        ),

        set(
            "Complex fragments",
            CbWsRecipeItem("Form", key = "cookbook:recipe:form"),
            CbWsRecipeItem("Canvas", key = "cookbook:recipe:canvas"),
            CbWsRecipeItem("Dialog", key = "cookbook:recipe:dialog"),
            CbWsRecipeItem("Paragraph", key = "cookbook:recipe:paragraph"),
            CbWsRecipeItem("Sidebar", key = "cookbook:recipe:sidebar"),
            CbWsRecipeItem("Snackbar", key = "cookbook:recipe:snackbar"),
            CbWsRecipeItem("Tree", key = "cookbook/tree/recipe"),
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
        CbWsContext.WSIT_CB_RECIPE_FOLDER,
        children = recipes.toList()
    )

