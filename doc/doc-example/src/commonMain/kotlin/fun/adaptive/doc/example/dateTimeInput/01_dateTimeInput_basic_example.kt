package `fun`.adaptive.doc.example.dateTimeInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.input.datetime.dateTimeInput
import `fun`.adaptive.ui.input.datetime.dateTimeInputBackend
import `fun`.adaptive.utility.localDateTime

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