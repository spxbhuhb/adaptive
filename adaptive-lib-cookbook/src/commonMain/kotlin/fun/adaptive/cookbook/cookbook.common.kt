package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.recipe.ui.layout.workspace.WorkspaceRecipePaneFragmentFactory
import `fun`.adaptive.cookbook.support.E
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.LibFragmentFactory
import `fun`.adaptive.ui.builtin.*
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

    contexts += CookbookContext(this)

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
        model = TODO()
    )

    toolPanes += WsPane(
        UUID(),
        "Recipe",
        Graphics.menu,
        WsPanePosition.Center,
        "cookbook:center",
        model = TODO()
    )

}