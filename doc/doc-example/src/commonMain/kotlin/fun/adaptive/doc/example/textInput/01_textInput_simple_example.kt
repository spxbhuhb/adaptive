package `fun`.adaptive.doc.example.textInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
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
