package `fun`.adaptive.ui.input.bool

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.onKeydown
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.spaceBetween
import `fun`.adaptive.ui.generated.resources.check
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
        // FIXME space between and maxwidth should not be in booleanInput
        instructions() .. spaceBetween .. maxWidth

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
                    svg(Graphics.check, theme.icon)
                }
            } else {
                box(theme.inactive) {

                }
            }
        }
    }

    return fragment()
}
