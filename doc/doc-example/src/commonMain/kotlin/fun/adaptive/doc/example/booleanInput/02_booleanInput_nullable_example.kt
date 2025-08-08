package `fun`.adaptive.doc.example.booleanInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.input.bool.booleanInput
import `fun`.adaptive.ui.input.bool.booleanInputBackend

/**
 * # Nullable
 *
 * - set `isNullable` of the backend to `true`
 */
@Adaptive
fun booleanInputNullableExample(): AdaptiveFragment {

    val viewBackend = booleanInputBackend(null).apply {
        label = "Nullable"
        isNullable = true
    }

    booleanInput(viewBackend)

    return fragment()
}
