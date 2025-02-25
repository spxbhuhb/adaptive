package `fun`.adaptive.cookbook.ui.layout.box

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun boxRecipe(): AdaptiveFragment {
    column {
        gap { 16.dp }
        growingChild()
        addChild()
        addChild(inColumn = true)
    }

    return fragment()
}


