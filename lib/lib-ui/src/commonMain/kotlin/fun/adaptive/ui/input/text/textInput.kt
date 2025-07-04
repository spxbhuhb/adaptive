package `fun`.adaptive.ui.input.text

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.multiLineTextInput
import `fun`.adaptive.ui.api.singleLineTextInput
import `fun`.adaptive.ui.input.decoratedInput

@Adaptive
fun textInput(
    viewBackend: TextInputViewBackend? = null,
    value: (() -> String?)? = null
): AdaptiveFragment {

    val observed = observe {
        (viewBackend ?: TextInputViewBackend(value?.invoke() ?: ""))
            .also { fragment().instructions.applyTo(it) }
    }

    val focus = focus()

    decoratedInput(focus, observed) {
        if (! observed.multiline) {
            singleLineTextInput(value = observed.inputValue, onChange = { v -> observed.inputValue = v }) ..
                observed.containerThemeInstructions(focus) ..
                observed.inputTheme.singleLine ..
                instructions()
        } else {
            multiLineTextInput(value = observed.inputValue, onChange = { v -> observed.inputValue = v }) ..
                observed.containerThemeInstructions(focus) ..
                observed.areaInstructions(focus) ..
                instructions()
        }
    }

    return fragment()
}