package `fun`.adaptive.sandbox

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace.Companion.wsContext
import `fun`.adaptive.ui.mpw.model.Pane

@Adaptive
fun cookbookCenter(pane: Pane<*>): AdaptiveFragment {
    val recipeKey = valueFrom { fragment().wsContext<CbWsContext>().activeRecipeKey }

    box {
        maxSize .. padding { 16.dp }

        if (recipeKey != null) {
            actualize(recipeKey)
        }
    }

    return fragment()
}