package `fun`.adaptive.cookbook.ui.layout.grid

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun gridExtendRecipe() {
    grid {
        rowTemplate(extend = 40.dp)
        colTemplate(40.dp, 40.dp)

        text(1)
        text(2)
        text(3)
    }
}