package `fun`.adaptive.ui.input.text

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.multiLineTextInput
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.InputTheme

@Adaptive
fun textInputArea(
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

    multiLineTextInput(value = value, onChange = onChange) .. themeInstructions .. instructions()

    return fragment()
}