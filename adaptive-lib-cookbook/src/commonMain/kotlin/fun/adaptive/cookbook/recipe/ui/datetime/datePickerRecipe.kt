package `fun`.adaptive.cookbook.recipe.ui.datetime

import `fun`.adaptive.cookbook.support.example
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.datetime.datePicker
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun datePickerRecipe(): AdaptiveFragment {

    column {
        gap { 16.dp } .. maxSize .. verticalScroll
        example("not fully functional yet:\nno feedback\nmonth and year select\ndoes not position correctly") {
            datePicker()
        }
    }

    return fragment()
}