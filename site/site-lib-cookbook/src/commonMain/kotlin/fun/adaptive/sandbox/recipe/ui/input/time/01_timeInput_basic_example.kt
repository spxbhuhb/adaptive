package `fun`.adaptive.sandbox.recipe.ui.input.time

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.input.date.dateInput
import `fun`.adaptive.ui.input.date.dateInputBackend
import `fun`.adaptive.ui.input.time.timeInput
import `fun`.adaptive.ui.input.time.timeInputBackend
import `fun`.adaptive.utility.localTime

/**
 * # Basic time input
 *
 * - Accepts `hour:minute` formatted time values, optionally with "AM"/"PM" postfix.
 * - When there is no postfix, the hour is treated as 24h.
 *
 * NOTE: The dropdown is not finished yet.
 */
@Adaptive
fun timeInputExample(): AdaptiveFragment {

    val backend = timeInputBackend {
        inputValue = localTime()
        label = "Time"
    }

    timeInput(backend)

    return fragment()
}