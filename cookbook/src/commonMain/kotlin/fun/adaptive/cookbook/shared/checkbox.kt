package `fun`.adaptive.cookbook.shared

import `fun`.adaptive.cookbook.Res
import `fun`.adaptive.cookbook.check
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.image
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.api.frame
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick

/**
 * Editor for a boolean.
 */
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
            box(*activeCheckBox) {
                image(Res.drawable.check) .. noSelect .. frame(1.dp, 1.dp, 18.dp, 18.dp)
            }
        } else {
            box(*inactiveCheckBox) {

            }
        }
    }

    return fragment()
}