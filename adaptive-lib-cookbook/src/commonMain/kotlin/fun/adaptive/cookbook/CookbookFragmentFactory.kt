package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.recipe.recipe1
import `fun`.adaptive.cookbook.recipe.recipe2
import `fun`.adaptive.cookbook.recipe.ui.snackbar.snackbarRecipe
import `fun`.adaptive.cookbook.recipe.ui.splitpane.splitPaneRecipe
import `fun`.adaptive.cookbook.recipe.ui.svg.svgRecipe
import `fun`.adaptive.cookbook.recipe.ui.text.textRecipe
import `fun`.adaptive.cookbook.recipe.ui.tree.treeRecipe
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object CookbookFragmentFactory : FoundationFragmentFactory() {
    init {
        add("cookbook:recipes", ::cookbookPane)
        add("cookbook:center", ::cookbookCenter)
        add("cookbook:recipe:recipe1", ::recipe1)
        add("cookbook:recipe:recipe2", ::recipe2)
        add("cookbook:recipe:snackbar", ::snackbarRecipe)
        add("cookbook:recipe:splitpane", ::splitPaneRecipe)
        add("cookbook:recipe:svg", ::svgRecipe)
        add("cookbook:recipe:text", ::textRecipe)
        add("cookbook:recipe:tree", ::treeRecipe)
    }
}