package `fun`.adaptive.ui.checkbox.api

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions

@Adaptive
fun checkbox(
    vararg instructions: AdaptiveInstruction,
    binding: AdaptiveStateVariableBinding<Boolean>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> Boolean
): AdaptiveFragment {
    checkNotNull(binding)
    boundCheckbox(instructions(), binding = binding)
    return fragment()
}