package `fun`.adaptive.ui.checkbox.api

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.builtin.Res
import `fun`.adaptive.ui.builtin.check
import `fun`.adaptive.ui.checkbox.api.theme.CheckboxTheme
import `fun`.adaptive.ui.checkbox.api.theme.checkboxTheme

@Adaptive
fun boundCheckbox(
    vararg instructions: AdaptiveInstruction,
    binding: AdaptiveStateVariableBinding<Boolean>,
    theme: CheckboxTheme = checkboxTheme
): AdaptiveFragment {

    row(*instructions) {
        onClick { binding.setValue(! binding.value, true) }

        if (binding.value) {
            box(*theme.active) {
                svg(Res.drawable.check, *theme.icon)
            }
        } else {
            box(*theme.inactive) {

            }
        }
    }

    return fragment()
}