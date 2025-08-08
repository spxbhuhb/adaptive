package `fun`.adaptive.doc.example.button

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.lib_app.generated.resources.lock
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.example
import `fun`.adaptive.ui.input.button.dangerButton
import `fun`.adaptive.ui.snackbar.failNotification

/**
 * # Button with danger theme
 *
 * Use danger buttons for dangerous operations such as delete.
 *
 * This lets the user know that the operation involves some risks and should be considered carefully.
 */
@Adaptive
fun buttonDangerExample(): AdaptiveFragment {

    dangerButton(Strings.example, Graphics.lock) { failNotification("Danger submit clicked!") }

    return fragment()
}