package `fun`.adaptive.ui.input.number

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.singleLineTextInput
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.InputTheme
import `fun`.adaptive.utility.format

@Adaptive
fun doubleOrNullInput(
    value: Double?,
    decimals: Int = 2,
    state: InputContext = InputContext(),
    theme: InputTheme = InputTheme.DEFAULT,
    onChange: (Double?) -> Unit
): AdaptiveFragment {
    val observed = valueFrom { state }
    val focus = focus()
    val formatted = value?.format(decimals, hideZeroDecimals = true)

    val themeInstructions = when {
        observed.disabled -> theme.disabled
        observed.invalid -> if (focus) theme.invalidFocused else theme.invalidNotFocused
        else -> if (focus) theme.focused else theme.enabled
    }

    singleLineTextInput(
        value = formatted,

        onChange = {

            if (it.isEmpty()) {
                onChange(null)
                return@singleLineTextInput
            }

            val inputValue = it.toDoubleOrNull()

            if (inputValue != null) {
                observed.invalid = false
                onChange(inputValue)
            } else {
                observed.invalid = true
            }
        }
    ) .. themeInstructions .. theme.singleLine .. alignItems.end .. instructions()

    return fragment()
}
