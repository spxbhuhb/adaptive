package `fun`.adaptive.doc.example.colorInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.input.color.colorInput
import `fun`.adaptive.ui.input.color.colorInputBackend

/**
 * # Color input
 *
 * - Click on the color to show the popup.
 * - Edit the text to change the color
 */
@Adaptive
fun colorInputExample(): AdaptiveFragment {

    val backend = colorInputBackend(color(0xff0000))

    colorInput(backend)

    return fragment()
}