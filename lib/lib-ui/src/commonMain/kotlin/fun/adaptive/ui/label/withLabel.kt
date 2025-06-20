package `fun`.adaptive.ui.label

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.input.InputContext

@Adaptive
fun withLabel(
    label: String,
    inputContext: InputContext = InputContext(),
    theme: LabelTheme = LabelTheme.DEFAULT,
    @Adaptive
    _KT_74337_content : (InputContext) -> Unit
): AdaptiveFragment {
    val observed = observe { inputContext }
    val focus = focus()

    val themeInstructions = when {
        observed.disabled -> theme.disabled
        observed.invalid -> if (focus || observed.isPopupOpen) theme.invalidFocused else theme.invalidNotFocused
        else -> if (focus || observed.isPopupOpen) theme.focused else theme.enabled
    }

    column(instructions()) {
        text(label, themeInstructions)
        _KT_74337_content(inputContext)
    }

    return fragment()
}