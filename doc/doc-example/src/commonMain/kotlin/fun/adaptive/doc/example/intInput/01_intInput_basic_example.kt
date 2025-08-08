package `fun`.adaptive.doc.example.intInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.integer.intInput
import `fun`.adaptive.ui.input.integer.intInputBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # Basic
 *
 * - non-nullable is the default
 * - non-nullable inputs consider empty input as invalid
 */
@Adaptive
fun intInputBasicExample() : AdaptiveFragment{

    val viewBackend = intInputBackend(123).apply {
        label = "Non-nullable"
    }

    intInput(viewBackend) .. width { 200.dp }

    return fragment()
}