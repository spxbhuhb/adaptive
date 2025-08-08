package `fun`.adaptive.doc.example.doubleInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.double_.doubleInput
import `fun`.adaptive.ui.input.double_.doubleInputBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # 8 decimals, nullable
 */
@Adaptive
fun doubleInput8dec(): AdaptiveFragment {

    val viewBackend = doubleInputBackend {
        inputValue = 12.34567890
        isNullable = true
        decimals = 8
    }

    doubleInput(viewBackend) .. width { 200.dp }

    return fragment()
}