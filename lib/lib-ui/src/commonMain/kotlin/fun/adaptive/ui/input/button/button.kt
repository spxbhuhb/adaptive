package `fun`.adaptive.ui.input.button

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.layout.AlignSelf
import `fun`.adaptive.ui.support.UiEventHandler

@Adaptive
fun button(
    label: String? = null,
    icon: GraphicsResourceSet? = null,
    viewBackend: ButtonViewBackend? = null,
    theme: ButtonTheme? = null,
    onClickFun: UiEventHandler? = null
): AdaptiveFragment {
    val focus = focus()

    // FIXME button instruction splitting is not reactive (dependency calculation problem)
    val i = fragment().instructions

    val observed = observe {
        (viewBackend ?: ButtonViewBackend(label)).also { backend ->
            theme?.let { backend.buttonTheme = it }
            i.applyTo(backend)
        }
    }

    row(observed.outerContainerInstructions(focus)) {
        AdaptiveInstructionGroup(i.filter { it is AlignSelf })

        row(observed.innerContainerInstructions(focus)) {
            AdaptiveInstructionGroup(i.filter { it !is AlignSelf }).let { filtered ->
                if (onClickFun != null) {
                    filtered.addAll(
                        onClick { e -> onClickFun.invoke(e) },
                        onEnter { e -> onClickFun.invoke(e) },
                    )
                } else {
                    filtered
                }
            }

            onKeyDown { observed.onKeydown(it) }
            if (icon != null) svg(icon) .. observed.iconThemeInstructions(focus)
            text(label) .. observed.textThemeInstructions(focus)
        }
    }

    return fragment()
}