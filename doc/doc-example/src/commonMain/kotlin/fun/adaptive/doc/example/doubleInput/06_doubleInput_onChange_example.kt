package `fun`.adaptive.doc.example.doubleInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.double_.doubleInput
import `fun`.adaptive.ui.input.double_.doubleInputBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.snackbar.infoNotification

/**
 * # onChange callback
 *
 * - set `onChange` to have a callback on value change
 * - 2 decimals, non-nullable (default)
 *
 * The callback is called only with valid values; when the input
 * is not a valid double, the callback is not called.
 *
 * Even when the field is non-nullable, the callback parameter type
 * is nullable. In this case you can safely assume that the value
 * is non-null.
 */
@Adaptive
fun doubleInputOnChange(): AdaptiveFragment {

    val viewBackend = doubleInputBackend {
        inputValue = 12.3
        onChange = { infoNotification("New value: $it") }
    }

    doubleInput(viewBackend) .. width { 200.dp }

    return fragment()
}