package `fun`.adaptive.sandbox.recipe.ui.input.datetime

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.datetime.dateInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.utility.localDate

@Adaptive
fun dateInputRecipe(): AdaptiveFragment {

    var date = localDate()

    column {
        padding { 16.dp } .. gap { 24.dp } .. maxSize .. verticalScroll

        //docDocument(Documents.date_input)

        withLabel("Normal") {
            dateInput(date, it) { v -> date = v }
        }

        withLabel("Disabled", InputContext(disabled = true)) {
            dateInput(date, it) { v -> date = v }
        }

        withLabel("Invalid", InputContext(invalid = true)) {
            dateInput(date, it) { v -> date = v }
        }
    }

    return fragment()
}