package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.recipe.ui.layout.workspace.WorkspaceRecipePaneFragmentFactory
import `fun`.adaptive.cookbook.support.E
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.LibFragmentFactory
import `fun`.adaptive.ui.builtin.collapseAll
import `fun`.adaptive.ui.builtin.collapse_all
import `fun`.adaptive.ui.builtin.expandAll
import `fun`.adaptive.ui.builtin.expand_all
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneAction
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

const val cookbookRecipePaneKey = "cookbook:recipes"

suspend fun cookbookCommon() {
    WireFormatRegistry["fun.adaptive.cookbook.support.E"] = EnumWireFormat(E.entries)
    commonMainStringsStringStore0.load()
}

fun AdaptiveAdapter.cookbookCommon() {
    fragmentFactory += arrayOf(CookbookFragmentFactory, WorkspaceRecipePaneFragmentFactory, LibFragmentFactory)
}

fun Workspace.cookbookCommon() {

    val context = CbWsContext(this)

    contexts += context

    toolPanes += WsPane(
        UUID(),
        "Cookbook",
        Graphics.flatware,
        WsPanePosition.LeftMiddle,
        cookbookRecipePaneKey,
        actions = listOf(
            WsPaneAction(
                Graphics.expand_all,
                Strings.expandAll,
            ) { w, p -> },
            WsPaneAction(
                Graphics.collapse_all,
                Strings.collapseAll,
            ) { w, p -> }
        ),
        model = Unit
    )

    addContentPaneBuilder(CbWsContext.WSIT_CB_RECIPE) { item ->
        WsPane(
            UUID(),
            item.name,
            context[item].icon,
            WsPanePosition.Center,
            "cookbook:center",
            controller = CookbookPaneController(context),
            model = item
        )
    }

    addItemConfig(CbWsContext.WSIT_CB_RECIPE, Graphics.dining)
    addItemConfig(CbWsContext.WSIT_CB_RECIPE_FOLDER, Graphics.dining)

}