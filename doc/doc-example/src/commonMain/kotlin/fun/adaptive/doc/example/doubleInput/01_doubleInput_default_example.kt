package `fun`.adaptive.doc.example.doubleInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.double_.doubleInput
import `fun`.adaptive.ui.input.double_.doubleInputBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # Default, only value set
 *
 * - non-nullable
 * - 2 decimals
 */
@Adaptive
fun doubleInputDefault(): AdaptiveFragment {

    val viewBackend = doubleInputBackend(12.3)

    doubleInput(viewBackend) .. width { 200.dp }

    return fragment()
}