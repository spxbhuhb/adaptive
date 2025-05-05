package `fun`.adaptive.ui.input.button

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.onKeydown
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text

@Adaptive
fun button(
    label: String? = null,
    icon: GraphicsResourceSet? = null,
    viewBackend: ButtonViewBackend? = null,
    theme: ButtonTheme? = null
): AdaptiveFragment {
    val focus = focus()

    val observed = valueFrom {
        (viewBackend ?: ButtonViewBackend(label)).also { backend ->
            theme?.let { backend.buttonTheme = it }
            fragment().instructions.applyTo(backend)
        }
    }

    row(observed.outerContainerInstructions(focus)) {
        row(observed.innerContainerInstructions(focus), instructions()) {
            onKeydown { observed.onKeydown(it) }
            if (icon != null) svg(icon) .. observed.iconThemeInstructions(focus)
            text(label) .. observed.textThemeInstructions(focus)
        }
    }
    return fragment()
}