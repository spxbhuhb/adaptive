package `fun`.adaptive.doc.example.booleanInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.input.bool.booleanInput
import `fun`.adaptive.ui.input.bool.booleanInputBackend

/**
 * # Basic
 *
 * - non-nullable is the default
 */
@Adaptive
fun booleanInputBasicExample(): AdaptiveFragment {

    val viewBackend = booleanInputBackend(true).apply {
        label = "Enabled"
    }

    booleanInput(viewBackend)

    return fragment()
}
