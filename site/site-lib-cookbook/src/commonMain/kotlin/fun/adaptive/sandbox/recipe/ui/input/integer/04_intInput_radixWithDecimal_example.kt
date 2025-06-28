package `fun`.adaptive.sandbox.recipe.ui.input.integer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.integer.intInput
import `fun`.adaptive.ui.input.integer.intInputBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # Radix 16 with decimal feedback
 *
 * - set `radix` to 16 to use hex notation
 * - set `showRadix10` to show the decimal value at the bottom
 */
@Adaptive
fun intInputRadixWithDecimalExample(): AdaptiveFragment {

    val inputBackend = intInputBackend(123).apply {
        label = "Radix 16, showing decimal value"
        radix = 16
        showRadix10 = true
    }

    intInput(inputBackend) .. width { 200.dp }

    return fragment()
}