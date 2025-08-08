package `fun`.adaptive.doc.example.booleanInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.input.bool.BooleanInputTheme
import `fun`.adaptive.ui.input.bool.booleanInput
import `fun`.adaptive.ui.input.bool.booleanInputBackend

/**
 * # Theme: small
 *
 * - set a theme on the backend
 */
@Adaptive
fun booleanInputThemeSmallExample(): AdaptiveFragment {

    val viewBackend = booleanInputBackend(true).apply {
        label = "Small theme"
        booleanInputTheme = BooleanInputTheme.small
    }

    booleanInput(viewBackend)

    return fragment()
}
