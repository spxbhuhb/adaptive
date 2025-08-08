package `fun`.adaptive.doc.example.button

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.generated.resources.save
import `fun`.adaptive.ui.input.button.submitButton
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.snackbar.infoNotification

/**
 * # Submit button, fix width
 *
 * Use a submit button to inform the user about the main operation.
 *
 * In this example the width of the button is manually set to 160.dp.
 */
@Adaptive
fun buttonSubmitExample(): AdaptiveFragment {

    submitButton(Strings.save) { infoNotification("Submit clicked!") } .. width { 160.dp }

    return fragment()
}