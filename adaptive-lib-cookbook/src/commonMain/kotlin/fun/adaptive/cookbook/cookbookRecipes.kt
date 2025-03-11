package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.model.WsRecipeItem
import `fun`.adaptive.cookbook.model.CookbookRecipeSet
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun cookbookRecipes(): AdaptiveFragment {
    val context = fragment().wsContext<WsCookbookContext>()
    val items = root.toTreeItem { item, modifiers -> showRecipe(context, item, modifiers) }.children

    wsToolPane(context.pane(cookbookRecipePaneKey)) {
        tree(items)
    }

    return fragment()
}

private fun showRecipe(context: WsCookbookContext, item: TreeItem, modifiers: Set<EventModifier>) {
    val safeItem = item.data as? WsRecipeItem ?: return

    context.workspace.addContent(safeItem, modifiers)
}

val root = CookbookRecipeSet(
    "Root",
    listOf(
        set(
            "Demos",
            WsRecipeItem("Good Morning", key = "cookbook:recipe:goodmorning"),
            WsRecipeItem("Markdown", key = "cookbook:recipe:markdown:demo"),
        ),

        set(
            "Layout",
            WsRecipeItem("Box", key = "cookbook:recipe:box"),
            WsRecipeItem("Grid", key = "cookbook:recipe:grid"),
            WsRecipeItem("Split Pane", key = "cookbook:recipe:splitpane"),
            WsRecipeItem("Tab", key = "cookbook:recipe:tab"),
            WsRecipeItem("Workspace", key = "cookbook:recipe:workspace"),
            WsRecipeItem("Wrap", key = "cookbook:recipe:wrap")
        ),

        set(
            "Basic fragments",
            WsRecipeItem("Button", key = "cookbook:recipe:button"),
            WsRecipeItem("Icon", key = "cookbook:recipe:icon"),
            WsRecipeItem("SVG", key = "cookbook:recipe:svg"),
            WsRecipeItem("Text", key = "cookbook:recipe:text"),
        ),

        set(
            "Textual fragments",
            WsRecipeItem("Code Fence", key = "cookbook:recipe:codefence"),
            WsRecipeItem("Paragraph", key = "cookbook:recipe:paragraph"),
            WsRecipeItem("Text", key = "cookbook:recipe:text")
        ),

        set(
            "Standalone inputs",
            WsRecipeItem("Checkbox", key = "cookbook:recipe:checkbox"),
            WsRecipeItem("Date picker", key = "cookbook:recipe:datepicker"),
            WsRecipeItem("Select", key = "cookbook:recipe:select"),
        ),

        set(
            "Bound inputs - editors",
            WsRecipeItem("Editor", key = "cookbook:recipe:editor"),
            WsRecipeItem("Form", key = "cookbook:recipe:form")
        ),

        set(
            "Complex fragments",
            WsRecipeItem("Canvas", key = "cookbook:recipe:canvas"),
            WsRecipeItem("Dialog", key = "cookbook:recipe:dialog"),
            WsRecipeItem("Paragraph", key = "cookbook:recipe:paragraph"),
            WsRecipeItem("Sidebar", key = "cookbook:recipe:sidebar"),
            WsRecipeItem("Snackbar", key = "cookbook:recipe:snackbar"),
            WsRecipeItem("Tree", key = "cookbook:recipe:tree"),
        ),

        set(
            "Other fragments",
            WsRecipeItem("Event", key = "cookbook:recipe:event"),
            WsRecipeItem("Popup", key = "cookbook:recipe:popup")
        )
    ),
    emptyList()
)

private fun set(name: String, vararg recipes: WsRecipeItem) =
    CookbookRecipeSet(
        name,
        emptyList(),
        recipes.toList()
    )

