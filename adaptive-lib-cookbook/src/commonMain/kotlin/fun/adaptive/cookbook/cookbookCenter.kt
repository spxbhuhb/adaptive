package `fun`.adaptive.cookbook

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext

@Adaptive
fun cookbookCenter() : AdaptiveFragment {
    val recipeKey = valueFrom { fragment().wsContext<CookbookContext>().activeRecipeKey }

    box {
        maxSize
        if (recipeKey != null) {
            actualize(recipeKey)
        }
    }

    return fragment()
}