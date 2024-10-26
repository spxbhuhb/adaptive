package `fun`.adaptive.cookbook.ui.grid

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.verticalScroll

@Adaptive
fun gridRecipe() {
    column {
        verticalScroll

        gridExtendRecipe()
        gridAlignRecipe()
        gridResizeRecipe()
    }
}