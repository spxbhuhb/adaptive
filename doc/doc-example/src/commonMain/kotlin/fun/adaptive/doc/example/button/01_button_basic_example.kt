package `fun`.adaptive.doc.example.button

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.example
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.snackbar.infoNotification

/**
 * # Basic button, name only
 */
@Adaptive
fun buttonBasicExample(): AdaptiveFragment {

    button(Strings.example) { infoNotification("Normal button clicked!") }

    return fragment()
}