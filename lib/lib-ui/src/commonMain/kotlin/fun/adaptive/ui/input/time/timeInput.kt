package `fun`.adaptive.ui.input.time

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.Independent
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.resource.language.localizedHourAndMinute
import `fun`.adaptive.resource.language.parseLocalizedHourAndMinuteOrNull
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.decoratedInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.utility.localTime
import kotlinx.datetime.LocalTime

@Adaptive
fun timeInput(
    viewBackend: TimeInputViewBackend
): AdaptiveFragment {

    val observed = observe { viewBackend }

    val focus = focus()
    val theme = observed.timeInputTheme

    decoratedInput(focus, observed) {

        timeInputInner(observed)

        // TODO finish time picker
//            if (! observed.isDisabled) {
//                primaryPopup(observed) { hide ->
//                    observed.timeInputTheme.dropdownPopup
//
//                    timePicker(
//                        observed.inputValue ?: LocalTime(8, 0),
//                        { observed.hidePopup?.invoke() },
//                        { v -> observed.inputValue = v },
//                        observed.timeInputTheme
//                    )
//                }
//            }
    }

    return fragment()
}

@Adaptive
internal fun timeInputInner(
    observed: TimeInputViewBackend,
) {
    val theme = observed.timeInputTheme
    val focus = focus()

    @Independent
    val time = observed.inputValue ?: localTime()

    column(instructions()) {

        singleLineTextInput(
            value = time.localizedHourAndMinute(),
            onChange = { v -> onChange(observed, v) }
        ) ..
            observed.containerThemeInstructions(focus) ..
            observed.inputTheme.singleLine ..
            theme.inputField
    }
}

private fun onChange(viewBackend: TimeInputViewBackend, value: String) {
    if (value.isEmpty()) {
        viewBackend.inputValue = null
        viewBackend.isInConversionError = ! viewBackend.isNullable
        return
    }

    val inputValue = value.parseLocalizedHourAndMinuteOrNull()

    if (inputValue != null) {
        viewBackend.isInConversionError = false
        viewBackend.inputValue = inputValue
    } else {
        viewBackend.isInConversionError = true
    }

    viewBackend.inputValue = inputValue
}