package `fun`.adaptive.sandbox.recipe.ui.input.datetime

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.input.datetime.dateTimeInput
import `fun`.adaptive.ui.input.datetime.dateTimeInputBackend
import `fun`.adaptive.ui.input.time.timeInput
import `fun`.adaptive.ui.input.time.timeInputBackend
import `fun`.adaptive.utility.localDateTime
import `fun`.adaptive.utility.localTime

/**
 * # DateTime input
 *
 * - Combination of date and time inputs.
 */
@Adaptive
fun dateTimeInputExample(): AdaptiveFragment {

    val backend = dateTimeInputBackend {
        inputValue = localDateTime()
        label = "Date and Time"
    }

    dateTimeInput(backend)

    return fragment()
}