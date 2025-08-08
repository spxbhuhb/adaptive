package `fun`.adaptive.doc.example.intInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.integer.intInput
import `fun`.adaptive.ui.input.integer.intInputBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # Radix 16
 *
 * - set `radix` to 16 to use hex notation
 */
@Adaptive
fun intInputRadixExample(): AdaptiveFragment {

    val inputBackend = intInputBackend(123).apply {
        label = "Radix 16, not showing radix 10"
        radix = 16
    }

    intInput(inputBackend) .. width { 200.dp }

    return fragment()
}