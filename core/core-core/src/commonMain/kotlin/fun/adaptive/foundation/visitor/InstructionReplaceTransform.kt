package `fun`.adaptive.foundation.visitor

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction

/**
 * Replaces the [original] instruction with the [new] instruction.
 */
class InstructionReplaceTransform(
    val original: AdaptiveInstruction,
    val new: AdaptiveInstruction
) : FragmentTransformerVoid() {

    override fun visitFragment(fragment: AdaptiveFragment): AdaptiveFragment? {
        var replaced = false

        val newInstructions = fragment.instructions.map {
            if (it == original) {
                replaced = true
                new
            } else {
                it
            }
        }

        if (replaced) {
            fragment.setStateVariable(0, newInstructions)
            fragment.patch()
        }

        return super.visitFragment(fragment)
    }
}