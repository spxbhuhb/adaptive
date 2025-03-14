package `fun`.adaptive.cookbook.recipe.ui.input

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.InputState
import `fun`.adaptive.ui.input.textInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.inputLabel
import `fun`.adaptive.ui.label.withLabel

@Adaptive
fun textInputRecipe(): AdaptiveFragment {
    var value = "Hello World!"

    column {
        maxHeight .. verticalScroll .. padding { 16.dp } .. width { 375.dp } .. gap { 16.dp }

        column {
            maxWidth
            inputLabel { "Default - separated label" }
            textInput(value) { value = it }
        }

        column {
            maxWidth
            inputLabel { "Disabled - separated label" }
            textInput(value, InputState(disabled = true)) { value = it }
        }

        column {
            maxWidth
            inputLabel { "Invalid - separated label" }
            textInput(value, InputState(invalid = true)) { value = it }
        }

        withLabel("Default - with label") { state ->
            textInput(value, state) { value = it }
        }

        withLabel("Disabled - with label", InputState(disabled = true)) { state ->
            textInput(value, state) { value = it }
        }

        withLabel("Invalid - with label", InputState(invalid = true)) { state ->
            textInput(value, state) { value = it }
        }
    }

    return fragment()
}