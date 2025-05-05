package `fun`.adaptive.ui.input.button

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.graphics.GraphicsResourceSet

@Adaptive
fun submitButton(
    label: String,
    icon: GraphicsResourceSet? = null
): AdaptiveFragment {
    button(label, icon, ButtonViewBackend(label).also { it.isSubmit = true }) .. instructions()
    return fragment()
}