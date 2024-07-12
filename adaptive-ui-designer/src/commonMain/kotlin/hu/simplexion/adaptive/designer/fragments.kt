package hu.simplexion.adaptive.designer

import hu.simplexion.adaptive.foundation.AdaptiveExpect
import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.manualImplementation
import hu.simplexion.adaptive.ui.common.instruction.DPixel

@AdaptiveExpect(designer)
fun dPixelInput(
    vararg instructions: AdaptiveInstruction,
    binding: AdaptiveStateVariableBinding<DPixel?>? = null,
    selector: () -> DPixel?
) {
    manualImplementation(instructions, binding, selector, selector)
}