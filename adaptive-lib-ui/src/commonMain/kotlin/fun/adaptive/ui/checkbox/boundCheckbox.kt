package `fun`.adaptive.ui.checkbox

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.builtin.check

@Adaptive
fun boundCheckbox(
    vararg instructions: AdaptiveInstruction,
    binding: AdaptiveStateVariableBinding<Boolean>,
    theme: CheckboxTheme = CheckboxTheme.DEFAULT
): AdaptiveFragment {

    row(instructions()) {
        onClick { binding.setValue(! binding.value, true) }

        if (binding.value) {
            box(theme.active) {
                svg(Graphics.check, theme.icon)
            }
        } else {
            box(theme.inactive) {

            }
        }
    }

    return fragment()
}