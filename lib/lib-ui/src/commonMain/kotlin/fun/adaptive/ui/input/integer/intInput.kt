package `fun`.adaptive.ui.input.integer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.singleLineTextInput
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.input.decoratedInput

@Adaptive
fun intInput(
    viewBackend: IntInputViewBackend
): AdaptiveFragment {

    val observed = valueFrom { viewBackend }
    val focus = focus()
    val formatted = observed.inputValue?.toString(viewBackend.radix)

    decoratedInput(focus, observed) {

        singleLineTextInput(
            value = formatted,
            onChange = { v ->

                if (v.isEmpty()) {
                    observed.inputValue = null
                    observed.isInConversionError = !observed.isNullable
                    return@singleLineTextInput
                }

                val inputValue = v.toIntOrNull(viewBackend.radix)

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

        if (viewBackend.showRadix10) {
            text(viewBackend.inputValue?.toString() ?: "-") .. viewBackend.inputTheme.endHint
        }
    }

    return fragment()
}
