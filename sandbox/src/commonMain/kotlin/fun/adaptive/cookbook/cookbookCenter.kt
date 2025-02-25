package `fun`.adaptive.cookbook

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize

@Adaptive
fun cookbookCenter() : AdaptiveFragment {
    val recipeKey = valueFrom { fragment().firstContext<CookbookContext>().activeRecipeKey }

    box {
        maxSize
        if (recipeKey != null) {
            actualize(recipeKey)
        }
    }
    return fragment()
}