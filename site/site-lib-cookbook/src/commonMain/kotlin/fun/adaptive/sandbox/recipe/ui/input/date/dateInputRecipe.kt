package `fun`.adaptive.sandbox.recipe.ui.input.date

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.date.dateInput
import `fun`.adaptive.ui.input.date.dateInputBackend
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun dateInputRecipe(): AdaptiveFragment {

    column {
        padding { 16.dp } .. gap { 24.dp } .. maxSize .. verticalScroll

        //docDocument(Documents.date_input)

        dateInput(dateInputBackend())
    }

    return fragment()
}