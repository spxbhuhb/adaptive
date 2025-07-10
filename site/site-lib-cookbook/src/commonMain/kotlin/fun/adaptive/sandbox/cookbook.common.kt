package `fun`.adaptive.sandbox

import `fun`.adaptive.cookbook.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.sandbox.recipe.ui.mpw.WorkspaceRecipePaneFragmentFactory
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.ui.LibFragmentFactory

const val cookbookRecipePaneKey = "cookbook:recipes"

suspend fun cookbookCommon() {

    commonMainStringsStringStore0.load()
}

fun AdaptiveAdapter.cookbookCommon() {
    fragmentFactory += arrayOf(CookbookFragmentFactory, WorkspaceRecipePaneFragmentFactory, LibFragmentFactory)
}
