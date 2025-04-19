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
import `fun`.adaptive.ui.input.decoratedInput
import `fun`.adaptive.utility.format

@Adaptive
fun doubleInput(
    value: Double,
    decimals: Int = 2,
    state: InputContext = InputContext(),
    theme: InputTheme = InputTheme.DEFAULT,
    onChange: (Double) -> Unit
): AdaptiveFragment {
    doubleOrNullInput(value, decimals, state, theme) { v ->
        if (v == null) {
            state.invalid = true
        } else {
            onChange(v)
            state.invalid = false
        }
    } .. instructions()
    return fragment()
}

@Adaptive
fun doubleInput2(
    viewBackend: DoubleInputViewBackend
): AdaptiveFragment {

    val observed = valueFrom { viewBackend } as DoubleInputViewBackend // FIXME selfobserved cast
    val focus = focus()
    val formatted = observed.inputValue?.format(observed.decimals, hideZeroDecimals = true)

    decoratedInput(focus, observed) {
        singleLineTextInput(
            value = formatted,
            onChange = { v ->

                if (v.isEmpty()) {
                    observed.inputValue = null
                    observed.isInConversionError = !observed.isNullable
                    return@singleLineTextInput
                }

                val inputValue = v.toDoubleOrNull()

                if (inputValue != null) {
                    observed.isInConversionError = false
                    observed.inputValue = inputValue
                } else {
                    observed.isInConversionError = true
                }
            }
        ) ..
            observed.containerThemeInstructions(focus) ..
            observed.inputTheme.singleLine ..
            alignItems.end ..
            instructions()
    }

    return fragment()
}
