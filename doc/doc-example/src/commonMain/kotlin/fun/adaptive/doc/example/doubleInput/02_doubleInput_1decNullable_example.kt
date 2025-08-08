package `fun`.adaptive.doc.example.doubleInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.double_.doubleInput
import `fun`.adaptive.ui.input.double_.doubleInputBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # 1 decimal, nullable
 *
 * - use `decimals` to set how many decimals the editor allows
 * - set `isNullable` to true to make the input treat empty input field as null
 *
 * NOTE: Double cannot store exact decimals, limiting the number
 * of the decimals works only when you use proper formatting. For example,
 * if a user types in `12.3` the actual number will be `12.30000004` or something
 * like that.
 */
@Adaptive
fun doubleInput1decNullable(): AdaptiveFragment {

    val viewBackend = doubleInputBackend(12.3) {
        isNullable = true
        decimals = 1
    }

    doubleInput(viewBackend) .. width { 200.dp }

    return fragment()
}