package `fun`.adaptive.ui.input.button

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.onClick

@Adaptive
fun submitButton(
    label: String? = null,
    icon: GraphicsResourceSet? = null,
    viewBackend: ButtonViewBackend? = null,
    onClickFun: (() -> Unit)? = null
): AdaptiveFragment {

    button(label, icon, (viewBackend ?: ButtonViewBackend(label)).also { it.isSubmit = true }) ..
        if (onClickFun != null) {
            instructions() .. onClick { onClickFun.invoke() }
        } else {
            instructions()
        }

    return fragment()
}