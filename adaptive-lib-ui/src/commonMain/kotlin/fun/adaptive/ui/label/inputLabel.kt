package `fun`.adaptive.ui.label

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.input.InputState


@Adaptive
fun inputLabel(
    inputState: InputState = InputState(),
    theme: LabelTheme = LabelTheme.DEFAULT,
    label: () -> String
): AdaptiveFragment {
    val observed = valueFrom { inputState }
    val focus = focus()

    val themeInstructions = when {
        observed.disabled -> theme.disabled
        observed.invalid -> if (focus) theme.invalidFocused else theme.invalidNotFocused
        else -> if (focus) theme.focused else theme.enabled
    }

    text(label(), themeInstructions, instructions())

    return fragment()
}