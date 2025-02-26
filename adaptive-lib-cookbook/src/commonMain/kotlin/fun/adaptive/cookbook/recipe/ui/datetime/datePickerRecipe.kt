package `fun`.adaptive.cookbook.recipe.ui.datetime

import `fun`.adaptive.cookbook.support.example
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.datetime.datePicker

@Adaptive
fun datePickerRecipe() : AdaptiveFragment {

    example("not fully functional yet:\nno feedback\nmonth and year select\ndoes not position correctly") {
        datePicker()
    }

    return fragment()
}