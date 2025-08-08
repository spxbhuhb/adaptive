package `fun`.adaptive.doc.example.timeRangeInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.lib.util.datetime.TimeRange
import `fun`.adaptive.ui.input.timerange.timeRangeInput
import `fun`.adaptive.ui.input.timerange.timeRangeInputBackend
import `fun`.adaptive.utility.localTime

/**
 * # TimeRange input
 *
 * - Two time inputs for start and end.
 */
@Adaptive
fun timeRangeInputExample(): AdaptiveFragment {

    val backend = timeRangeInputBackend {
        inputValue = TimeRange(localTime(), localTime())
        label = "Start and end time"
    }

    timeRangeInput(backend)

    return fragment()
}