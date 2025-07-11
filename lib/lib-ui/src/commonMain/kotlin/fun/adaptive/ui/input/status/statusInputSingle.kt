package `fun`.adaptive.ui.input.status

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.check
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.input.decoratedInput
import `fun`.adaptive.ui.instruction.event.Keys

@Adaptive
fun statusInputSingle(
    viewBackend: StatusInputSingleViewBackend
): AdaptiveFragment {

    val observed = observe { viewBackend }
    val focus = focus()
    val theme = observed.booleanInputTheme

    decoratedInput(focus, observed) {
        instructions() .. theme.decoration

        row(theme.container) {

            onClick { observed.toggle() }

            onKeydown { event ->
                if (event.keyInfo?.key == Keys.SPACE) {
                    event.preventDefault()
                    observed.toggle()
                }
            }

            if (observed.inputValue?.contains(viewBackend.status) == true) {
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
