package `fun`.adaptive.ui.input.double_

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.singleLineTextInput
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.input.decoratedInput
import `fun`.adaptive.utility.format

@Adaptive
fun doubleInput(
    viewBackend: DoubleInputViewBackend
): AdaptiveFragment {

    val observed = observe { viewBackend }
    val focus = focus()
    val formatted = observed.inputValue?.format(observed.decimals, hideZeroDecimals = true)

    decoratedInput(focus, observed) {
        singleLineTextInput(
            value = formatted,
            onChange = { v ->

                if (v.isEmpty()) {
                    observed.inputValue = null
                    observed.isInConversionError = ! observed.isNullable
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

        if (observed.unit != null) {
            text(observed.unit) .. observed.inputTheme.endHint
        }
    }

    return fragment()
}
