package `fun`.adaptive.sandbox.recipe.ui.checkbox

import `fun`.adaptive.sandbox.support.example
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.checkbox.checkbox
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.withLabel

@Adaptive
fun checkboxRecipe() : AdaptiveFragment {

    var v1 = true
    var v2 = false
    var v3 = true

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

        example("with label") {
            withLabel("true initial value") {
                alignItems.center .. gap { 8.dp }
                checkbox(v3) { v -> v3 = v }
            }
        }

    }

    return fragment()
}
