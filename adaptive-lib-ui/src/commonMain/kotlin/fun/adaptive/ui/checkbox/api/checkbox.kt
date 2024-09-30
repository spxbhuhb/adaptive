package `fun`.adaptive.ui.checkbox.api

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.frame
import `fun`.adaptive.ui.api.image
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.builtin.Res
import `fun`.adaptive.ui.builtin.check
import `fun`.adaptive.ui.instruction.dp

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
                image(Res.drawable.check) .. noSelect .. frame(1.dp, 1.dp, 18.dp, 18.dp)
            }
        } else {
            box(*checkboxTheme.inactive) {

            }
        }
    }

    return fragment()
}