package `fun`.adaptive.ui.input.date

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.decoratedInput
import `fun`.adaptive.utility.localDate

@Adaptive
fun dateInput(
    viewBackend: DateInputViewBackend
): AdaptiveFragment {

    val observed = observe { viewBackend }
    val focus = focus()

    decoratedInput(focus, observed) {
        dateInputInner(observed)
    }

    return fragment()
}

@Adaptive
internal fun dateInputInner(
    observed: DateInputViewBackend
) {
    val focus = focus()
    val theme = observed.inputTheme

    column {
        observed.containerThemeInstructions(focus) .. height { theme.inputHeightDp }
        alignItems.bottomCenter

        text((observed.inputValue ?: localDate()).toString()) .. theme.inputFont

        if (! observed.isDisabled) {
            primaryPopup(observed) { hide ->
                observed.dateInputTheme.dropdownPopup
                focusFirst

                datePicker(
                    observed.inputValue ?: localDate(),
                    { hide() },
                    { v -> observed.inputValue = v },
                    observed.dateInputTheme
                )
            }
        }
    }
}