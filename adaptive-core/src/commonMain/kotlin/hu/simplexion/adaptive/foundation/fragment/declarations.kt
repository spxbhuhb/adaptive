/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.fragment

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveExpect
import hu.simplexion.adaptive.foundation.TestedInPlugin
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.manualImplementation

@AdaptiveExpect(foundation)
@TestedInPlugin
fun slot(name : String, @Adaptive initialContent : () -> Unit) {
    manualImplementation(name, initialContent)
}

@AdaptiveExpect(foundation)
@Adaptive
fun measureFragmentTime(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit) {
    manualImplementation(instructions, content)
}