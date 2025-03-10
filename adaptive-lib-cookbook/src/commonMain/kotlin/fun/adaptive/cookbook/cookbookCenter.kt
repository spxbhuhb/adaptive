package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.model.WsRecipeItem
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.api.firstContextOrNull
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.workspace.model.WsPane

@Adaptive
fun cookbookCenter() : AdaptiveFragment {
    val recipeItem = fragment().firstContextOrNull<WsPane<WsRecipeItem>>()?.model

    box {
        maxSize .. padding { 16.dp }

        if (recipeItem != null) {
            actualize(recipeItem.key)
        }
    }

    return fragment()
}