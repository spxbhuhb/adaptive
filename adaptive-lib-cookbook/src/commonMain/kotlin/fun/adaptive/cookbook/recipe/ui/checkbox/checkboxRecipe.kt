package `fun`.adaptive.cookbook.recipe.ui.checkbox

import `fun`.adaptive.cookbook.support.example
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.checkbox.api.checkbox
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun checkboxRecipe() : AdaptiveFragment {

    var v1 = true
    var v2 = false

    column {
        gap { 16.dp } .. maxSize .. verticalScroll

        example("true initial value") {
            row {
                gap { 16.dp }
                checkbox(v1) { v1 = it }
                text("v1: $v1")
            }
        }

        example("false initial value") {
            row {
                gap { 16.dp }
                checkbox(v2) { v2 = it }
                text("v2: $v2")
            }
        }

    }

    return fragment()
}
