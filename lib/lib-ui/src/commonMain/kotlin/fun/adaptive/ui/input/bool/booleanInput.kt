package `fun`.adaptive.ui.input.bool

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.check
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.input.decoratedInput
import `fun`.adaptive.ui.instruction.event.Keys

@Adaptive
fun booleanInput(
    viewBackend: BooleanInputViewBackend
): AdaptiveFragment {

    val observed = valueFrom { viewBackend }
    val focus = focus()
    val theme = observed.booleanInputTheme

    decoratedInput(focus, observed) {
        instructions() .. spaceBetween

        row(theme.container) {

            onClick { observed.inputValue = (observed.inputValue != true) }

            onKeydown { event ->
                if (event.keyInfo?.key == Keys.SPACE) {
                    event.preventDefault()
                    observed.inputValue = (observed.inputValue != true)
                }
            }

            if (observed.inputValue == true) {
                box(theme.active) {
                    icon(Graphics.check, theme.icon)
                }
            } else {
                box(theme.inactive) {

                }
            }
        }
    }

    return fragment()
}
