package `fun`.adaptive.ui.input.long_

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.singleLineTextInput
import `fun`.adaptive.ui.input.decoratedInput

@Adaptive
fun longInput(
    viewBackend: LongInputViewBackend
): AdaptiveFragment {

    val observed = observe { viewBackend }
    val focus = focus()
    val formatted = observed.inputValue?.toString()

    decoratedInput(focus, observed) {
        singleLineTextInput(
            value = formatted,
            onChange = { v ->

                if (v.isEmpty()) {
                    observed.inputValue = null
                    observed.isInConversionError = !observed.isNullable
                    return@singleLineTextInput
                }

                val inputValue = v.toLongOrNull()

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
