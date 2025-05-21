package `fun`.adaptive.ui.input.dpixel

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.singleLineTextInput
import `fun`.adaptive.ui.input.decoratedInput
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.utility.format

@Adaptive
fun dPixelInput(
    viewBackend: DPixelInputViewBackend
): AdaptiveFragment {

    val observed = valueFrom { viewBackend }
    val focus = focus()
    val formatted = observed.inputValue?.value?.format(observed.decimals, hideZeroDecimals = true)

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
                    observed.inputValue = DPixel(inputValue)
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
