package `fun`.adaptive.cookbook.ui.layout.box

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun boxRecipe() {
    column {
        gap { 16.dp }
        growingChild()
        addChild()
        addChild(inColumn = true)
    }
}


