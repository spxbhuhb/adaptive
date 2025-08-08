package `fun`.adaptive.doc.example.layout.grid

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.verticalScroll

@Adaptive
fun gridRecipe(): AdaptiveFragment {
    column {
        maxSize .. verticalScroll

        gridExtendRecipe()
        gridAlignRecipe()
        gridResizeRecipe()
    }

    return fragment()
}