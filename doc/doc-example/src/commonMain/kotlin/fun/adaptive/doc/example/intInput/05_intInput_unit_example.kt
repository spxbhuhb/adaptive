package `fun`.adaptive.doc.example.intInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.integer.intInput
import `fun`.adaptive.ui.input.integer.intInputBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # Unit
 *
 * - set `unit` of the backend to display a unit after the field
 */
@Adaptive
fun intInputUnitExample() : AdaptiveFragment {

    val viewBackend = intInputBackend(123).apply {
        unit = "m²"
    }

    intInput(viewBackend) .. width { 200.dp }

    return fragment()
}
