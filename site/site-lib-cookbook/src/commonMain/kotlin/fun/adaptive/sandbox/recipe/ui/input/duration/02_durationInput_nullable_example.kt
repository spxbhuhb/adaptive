package `fun`.adaptive.sandbox.recipe.ui.input.duration

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.input.duration.durationInput
import `fun`.adaptive.ui.input.duration.durationInputBackend
import kotlin.time.Duration.Companion.hours

/**
 * # Nullable
 */
@Adaptive
fun durationInputNullable(): AdaptiveFragment {

    val backend = durationInputBackend {
        inputValue = 23.hours
        isNullable = true
        label = "Duration"
    }

    durationInput(backend)

    return fragment()
}