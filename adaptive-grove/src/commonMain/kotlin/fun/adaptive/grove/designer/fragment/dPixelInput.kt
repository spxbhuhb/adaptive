package `fun`.adaptive.grove.designer.fragment

import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.grove.grove
import `fun`.adaptive.ui.instruction.DPixel

@AdaptiveExpect(grove)
fun dPixelInput(
    vararg instructions: AdaptiveInstruction,
    binding: AdaptiveStateVariableBinding<DPixel?>? = null,
    selector: () -> DPixel?
) {
    manualImplementation(instructions, binding, selector, selector)
}