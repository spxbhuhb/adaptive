package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.recipe.ui.layout.workspace.WorkspaceRecipePaneFragmentFactory
import `fun`.adaptive.cookbook.support.E
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.LibFragmentFactory
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneController
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

const val cookbookRecipePaneKey = "cookbook:recipes"

suspend fun cookbookCommon() {

    commonMainStringsStringStore0.load()
}

fun AdaptiveAdapter.cookbookCommon() {
    fragmentFactory += arrayOf(CookbookFragmentFactory, WorkspaceRecipePaneFragmentFactory, LibFragmentFactory)
}
