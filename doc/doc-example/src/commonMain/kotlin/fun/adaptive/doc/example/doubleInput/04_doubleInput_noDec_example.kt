package `fun`.adaptive.doc.example.doubleInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.double_.doubleInput
import `fun`.adaptive.ui.input.double_.doubleInputBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # No decimals
 *
 * - Set `decimals` to `0` to allow only whole numbers.
 */
@Adaptive
fun doubleInputNoDec(): AdaptiveFragment {

    val viewBackend = doubleInputBackend(12.3) {
        decimals = 0
    }

    doubleInput(viewBackend) .. width { 200.dp }

    return fragment()
}