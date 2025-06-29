package `fun`.adaptive.sandbox.recipe.ui.input.button

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.example
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.input.button.buttonBackend

/**
 * # Button with disabled theme
 *
 * To make a button disabled and apply the theme, pass a backend with `disabled = true`.
 */
@Adaptive
fun buttonDisabledExample(): AdaptiveFragment {

    button(Strings.example, viewBackend = buttonBackend { disabled = true })

    return fragment()
}