package `fun`.adaptive.cookbook

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.builtin.menu
import `fun`.adaptive.ui.builtin.settings
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.WorkspacePane
import `fun`.adaptive.ui.workspace.WorkspacePanePosition
import `fun`.adaptive.utility.UUID

const val cookbookRecipePaneKey = "cookbook:recipes"

fun AdaptiveAdapter.cookbookCommon() {
    fragmentFactory += arrayOf(CookbookFragmentFactory)
}

fun Workspace.cookbookCommon() {

    contexts += CookbookContext(this)

    panes += WorkspacePane(
        UUID(),
        "Cookbook",
        Graphics.flatware,
        WorkspacePanePosition.LeftMiddle,
        cookbookRecipePaneKey
    )

    panes += WorkspacePane(
        UUID(),
        "Recipe",
        Graphics.menu,
        WorkspacePanePosition.Center,
        "cookbook:center"
    )

}