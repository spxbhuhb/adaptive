package `fun`.adaptive.ui.label

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.tabIndex
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.input.InputState
import `fun`.adaptive.utility.debug

@Adaptive
fun withLabel(
    label: String,
    inputState: InputState = InputState(),
    theme: LabelTheme = LabelTheme.DEFAULT,
    @Adaptive
    _KT_74337_content : (InputState) -> Unit
): AdaptiveFragment {
    val observed = valueFrom { inputState }
    val focus = focus()

    val themeInstructions = when {
        observed.disabled -> theme.disabled
        observed.invalid -> if (focus) theme.invalidFocused else theme.invalidNotFocused
        else -> if (focus) theme.focused else theme.enabled
    }

    column(instructions()) {
        tabIndex { 0 }
        text(label, themeInstructions)
        _KT_74337_content(inputState)
    }

    return fragment()
}