package `fun`.adaptive.cookbook.recipe

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.text

@Adaptive
fun recipe1() : AdaptiveFragment {
    text("Recipe 1")
    return fragment()
}