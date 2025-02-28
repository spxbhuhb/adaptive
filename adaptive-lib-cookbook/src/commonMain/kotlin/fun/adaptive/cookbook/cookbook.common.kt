package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.recipe.ui.workspace.WorkspaceRecipePaneFragmentFactory
import `fun`.adaptive.cookbook.support.E
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.LibFragmentFactory
import `fun`.adaptive.ui.builtin.*
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.WorkspacePane
import `fun`.adaptive.ui.workspace.WorkspacePaneAction
import `fun`.adaptive.ui.workspace.WorkspacePanePosition
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

    panes += WorkspacePane(
        UUID(),
        "Cookbook",
        Graphics.flatware,
        WorkspacePanePosition.LeftMiddle,
        cookbookRecipePaneKey,
        actions = listOf(
            WorkspacePaneAction(
                Graphics.expand_all,
                Strings.expandAll,
            ) { w, p -> },
            WorkspacePaneAction(
                Graphics.collapse_all,
                Strings.collapseAll,
            ) { w, p -> }
        )
    )

    panes += WorkspacePane(
        UUID(),
        "Recipe",
        Graphics.menu,
        WorkspacePanePosition.Center,
        "cookbook:center"
    )

}