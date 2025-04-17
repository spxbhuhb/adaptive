package `fun`.adaptive.ui.input.text

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.singleLineTextInput
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.InputTheme
import `fun`.adaptive.ui.input.InputViewBackend
import `fun`.adaptive.ui.input.decoratedInput

@Adaptive
fun textInput(
    value : String?,
    state : InputContext = InputContext(),
    theme : InputTheme = InputTheme.DEFAULT,
    onChange : (String) -> Unit
) : AdaptiveFragment {
    val observed = valueFrom { state }
    val focus = focus()

    val themeInstructions = when {
        observed.disabled -> theme.disabled
        observed.invalid -> if (focus) theme.invalidFocused else theme.invalidNotFocused
        else -> if (focus) theme.focused else theme.enabled
    }

    singleLineTextInput(value = value, onChange = onChange) .. themeInstructions .. theme.singleLine .. instructions()

    return fragment()
}

@Adaptive
fun textInput2(
    viewBackend: InputViewBackend<String>
): AdaptiveFragment {
    val observed = valueFrom { viewBackend }
    val focus = focus()

    println("textInput2: ${observed.inputValue} ${observed.isInvalid}")

    decoratedInput(focus, observed) {
        singleLineTextInput(value = observed.inputValue, onChange = { v -> observed.inputValue = v }) ..
            observed.themeInstructions(focus) ..
            observed.inputTheme.singleLine ..
            instructions()
    }

    return fragment()
}