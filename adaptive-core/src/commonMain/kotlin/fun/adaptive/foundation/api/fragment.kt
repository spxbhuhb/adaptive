package `fun`.adaptive.foundation.api

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.fragment.foundation
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.manualImplementation

@AdaptiveExpect(foundation)
fun <T> localContext(context : T, vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit) {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(foundation)
fun measureFragmentTime(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit) {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(foundation)
fun actualize(key : String) {
    manualImplementation(key)
}