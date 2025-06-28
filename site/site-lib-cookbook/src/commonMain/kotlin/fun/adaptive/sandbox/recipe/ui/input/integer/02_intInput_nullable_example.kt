package `fun`.adaptive.sandbox.recipe.ui.input.integer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.integer.intInput
import `fun`.adaptive.ui.input.integer.intInputBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # Nullable
 *
 * - set `isNullable` of the backend to `true`
 * - nullable inputs consider empty input value as null
 */
@Adaptive
fun intInputNullableExample() : AdaptiveFragment {

    val viewBackend = intInputBackend(123).apply {
        label = "Nullable"
        isNullable = true
    }

    intInput(viewBackend) .. width { 200.dp }

    return fragment()
}
