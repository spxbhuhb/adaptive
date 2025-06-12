package `fun`.adaptive.ui.input.button

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.onKeydown
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.layout.AlignSelf

@Adaptive
fun button(
    label: String? = null,
    icon: GraphicsResourceSet? = null,
    viewBackend: ButtonViewBackend? = null,
    theme: ButtonTheme? = null,
    onClickFun: (() -> Unit)? = null
): AdaptiveFragment {
    val focus = focus()

    // FIXME button instruction splitting is not reactive (dependency calculation problem)
    val i = fragment().instructions

    val observed = valueFrom {
        (viewBackend ?: ButtonViewBackend(label)).also { backend ->
            theme?.let { backend.buttonTheme = it }
            i.applyTo(backend)
        }
    }

    row(observed.outerContainerInstructions(focus)) {
        AdaptiveInstructionGroup(i.filter { it is AlignSelf })

        row(observed.innerContainerInstructions(focus)) {
            AdaptiveInstructionGroup(i.filter { it !is AlignSelf }).let { if (onClickFun != null) it .. onClick { onClickFun.invoke() } else it }

            onKeydown { observed.onKeydown(it) }
            if (icon != null) svg(icon) .. observed.iconThemeInstructions(focus)
            text(label) .. observed.textThemeInstructions(focus)
        }
    }

    return fragment()
}