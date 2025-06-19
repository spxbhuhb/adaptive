package `fun`.adaptive.ui.input.badge

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.badge.badge
import `fun`.adaptive.ui.generated.resources.add
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseIconTheme
import `fun`.adaptive.ui.input.decoratedInput
import `fun`.adaptive.ui.instruction.event.Keys

@Adaptive
fun badgeInput(
    viewBackend: BadgeInputViewBackend
): AdaptiveFragment {

    val observed = valueFrom { viewBackend }
    val focus = focus()
    val theme = observed.badgeInputTheme
    var textInputValue = ""

    decoratedInput(focus, observed) {
        instructions()

        column(theme.container) {
            row {
                theme.inputContainer

                singleLineTextInput(value = textInputValue, onChange = { v -> textInputValue = v }) ..
                    observed.containerThemeInstructions(focus) ..
                    observed.inputTheme.singleLine ..
                    onKeydown { event ->
                        if (event.keyInfo?.key == Keys.ENTER) {
                            event.preventDefault()
                            viewBackend.addBadge(textInputValue)
                            textInputValue = ""
                        }
                    }

                actionIcon(Graphics.add, theme = denseIconTheme) { viewBackend.addBadge(textInputValue); textInputValue = "" }
            }

            flowBox {
                theme.badgeContainer
                for (label in observed.inputValue ?: emptySet()) {
                    badge(
                        label,
                        removable = (label !in viewBackend.unremovable),
                        theme = theme.badgeTheme
                    ) {
                        name -> viewBackend.removeBadge(name)
                    }
                }
            }
        }
    }

    return fragment()
}
