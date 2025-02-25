package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.model.CookbookRecipe
import `fun`.adaptive.cookbook.model.CookbookRecipeSet
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.tree.tree

@Adaptive
fun cookbookPane() : AdaptiveFragment {
    val context = fragment().firstContext<CookbookContext>()
    val items = root.toTreeItem { context.activeRecipeKey.value = (it.data as String) }.children

    tree(items)

    return fragment()
}

val root = CookbookRecipeSet(
    "Root",
    emptyList(),
    listOf(
        CookbookRecipe("Recipe 1", "cookbook:recipe:recipe1"),
        CookbookRecipe("Recipe 2", "cookbook:recipe:recipe2")
    )
)

