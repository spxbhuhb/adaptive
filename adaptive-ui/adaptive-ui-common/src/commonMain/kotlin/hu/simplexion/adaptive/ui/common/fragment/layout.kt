/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveExpect
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.manualImplementation
import hu.simplexion.adaptive.ui.common.common

@AdaptiveExpect(common)
fun box(vararg instructions : AdaptiveInstruction, @Adaptive content : () -> Unit) {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(common)
fun row(vararg instructions : AdaptiveInstruction, @Adaptive content : () -> Unit) {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(common)
fun column(vararg instructions : AdaptiveInstruction, @Adaptive content : () -> Unit) {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(common)
fun grid(vararg instructions : AdaptiveInstruction, @Adaptive content : () -> Unit) {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(common)
fun stack(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit) {
    manualImplementation(instructions, content)
}
