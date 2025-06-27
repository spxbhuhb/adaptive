package `fun`.adaptive.ui.input.date

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.decoratedInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.utility.localDate

@Adaptive
fun dateInput(
    viewBackend: DateInputViewBackend
): AdaptiveFragment {

    val observed = observe { viewBackend }
    val focus = focus()
    val theme = observed.inputTheme

    decoratedInput(focus, observed) {
        column(instructions()) {
            observed.containerThemeInstructions(focus)

            text(observed.inputValue.toString()) .. alignSelf.startCenter .. theme.inputFont

            if (! observed.isDisabled) {
                primaryPopup(observed) { hide ->
                    observed.dateInputTheme.dropdownPopup

                    datePicker(
                        observed.inputValue ?: localDate(),
                        { observed.hidePopup?.invoke() },
                        { v -> observed.inputValue = v },
                        observed.dateInputTheme
                    )
                }
            }
        }
    }

    return fragment()
}