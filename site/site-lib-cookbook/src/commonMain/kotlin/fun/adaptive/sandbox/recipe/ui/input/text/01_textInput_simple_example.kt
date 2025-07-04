package `fun`.adaptive.sandbox.recipe.ui.input.text

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.input.text.textInputBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # Simple text input
 *
 * - text inputs use [maxWidth](instruction://) by default, set a width to make them smaller
 */
@Adaptive
fun textInputSimpleExample(): AdaptiveFragment {

    val backend = textInputBackend("Hello World!")

    textInput(backend) .. width { 200.dp }

    return fragment()
}
