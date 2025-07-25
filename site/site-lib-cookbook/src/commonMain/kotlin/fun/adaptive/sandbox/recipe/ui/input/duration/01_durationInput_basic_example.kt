package `fun`.adaptive.sandbox.recipe.ui.input.duration

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.input.duration.durationInput
import `fun`.adaptive.ui.input.duration.durationInputBackend
import kotlin.time.Duration.Companion.minutes

/**
 * # Duration input
 *
 * - Non-nullable
 * - The unit is automatically selected by the input.
 */
@Adaptive
fun durationInputBasic(): AdaptiveFragment {

    val backend = durationInputBackend {
        inputValue = 12.minutes
        label = "Duration"
    }

    durationInput(backend)

    return fragment()
}