package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.recipe.recipe1
import `fun`.adaptive.cookbook.recipe.recipe2
import `fun`.adaptive.cookbook.recipe.ui.tree.treeRecipe
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object CookbookFragmentFactory : FoundationFragmentFactory() {
    init {
        add("cookbook:recipes", ::cookbookPane)
        add("cookbook:center", ::cookbookCenter)
        add("cookbook:recipe:recipe1", ::recipe1)
        add("cookbook:recipe:recipe2", ::recipe2)
        add("cookbook:recipe:tree", ::treeRecipe)
    }
}