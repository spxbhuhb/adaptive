package `fun`.adaptive.sandbox.recipe.ui.input.double_

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.double_.doubleInput
import `fun`.adaptive.ui.input.double_.doubleInputBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # Unit
 *
 * - Set `unit` to display a unit after the field
 */
@Adaptive
fun doubleInputUnit(): AdaptiveFragment {

    val viewBackend = doubleInputBackend(12.3) {
        unit = "m²"
    }

    doubleInput(viewBackend) .. width { 200.dp }

    return fragment()
}