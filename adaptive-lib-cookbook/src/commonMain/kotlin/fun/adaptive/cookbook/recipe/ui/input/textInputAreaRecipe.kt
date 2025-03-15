package `fun`.adaptive.cookbook.recipe.ui.input

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.InputState
import `fun`.adaptive.ui.input.textInput
import `fun`.adaptive.ui.input.textInputArea
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.inputLabel
import `fun`.adaptive.ui.label.withLabel

@Adaptive
fun textInputAreaRecipe(): AdaptiveFragment {
    var value = "Hello World!"

    column {
        maxHeight .. verticalScroll .. padding { 16.dp } .. width { 375.dp } .. gap { 16.dp }

        column {
            maxWidth
            inputLabel { "Default - separated label" }
            textInputArea(value) { value = it } .. height { 100.dp }
        }

        column {
            maxWidth
            inputLabel { "Disabled - separated label" }
            textInputArea(value, InputState(disabled = true)) { value = it } .. height { 100.dp }
        }

        column {
            maxWidth
            inputLabel { "Invalid - separated label" }
            textInputArea(value, InputState(invalid = true)) { value = it } .. height { 100.dp }
        }

        withLabel("Default - with label") { state ->
            textInputArea(value, state) { value = it } .. height { 100.dp }
        }

        withLabel("Disabled - with label", InputState(disabled = true)) { state ->
            textInputArea(value, state) { value = it } .. height { 100.dp }
        }

        withLabel("Invalid - with label", InputState(invalid = true)) { state ->
            textInputArea(value, state) { value = it } .. height { 100.dp }
        }
    }

    return fragment()
}