package `fun`.adaptive.ui.checkbox.api

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.builtin.Res
import `fun`.adaptive.ui.builtin.check
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.iconColors

@Adaptive
fun checkbox(
    vararg instructions: AdaptiveInstruction,
    binding: AdaptiveStateVariableBinding<Boolean>? = null,
    selector: () -> Boolean
): AdaptiveFragment {
    checkNotNull(binding)

    row(*instructions) {
        onClick { binding.setValue(! binding.value, true) }

        if (binding.value) {
            box(*checkboxTheme.active) {
                svg(Res.drawable.check) .. noSelect .. position(1.dp, 1.dp) .. svgHeight(17.dp) .. svgWidth(17.dp) .. iconColors.onPrimary
            }
        } else {
            box(*checkboxTheme.inactive) {

            }
        }
    }

    return fragment()
}