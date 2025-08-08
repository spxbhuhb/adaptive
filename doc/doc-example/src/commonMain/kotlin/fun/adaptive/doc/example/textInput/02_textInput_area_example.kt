package `fun`.adaptive.doc.example.textInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.input.text.textInputBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # Text input area
 *
 * - use `multiline = true` to make the input accept multiple lines
 * - multiline uses [maxSize](instruction://) by default
 *   - set size directly, or
 *   - put it into a sized container
 * - in browser multiline text input uses a text area
 */
@Adaptive
fun textInputAreaExample(): AdaptiveFragment {

    val backend = textInputBackend("Hello World!") {
        multiline = true
    }

    textInput(backend) .. height { 100.dp } .. width { 200.dp }

    return fragment()
}
