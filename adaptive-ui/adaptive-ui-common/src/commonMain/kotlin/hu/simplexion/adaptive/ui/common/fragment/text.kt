/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.AdaptiveExpect
import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.manualImplementation
import hu.simplexion.adaptive.ui.common.commonUI

@AdaptiveExpect(commonUI)
fun text(content: Any?, vararg instructions : AdaptiveInstruction) {
    manualImplementation(content, instructions)
}

/**
 * Editor for simple string.
 */
@AdaptiveExpect(commonUI)
fun stringEditor(
    vararg instructions : AdaptiveInstruction,
    binding: AdaptiveStateVariableBinding<String>? = null,
    selector: () -> String
) {
    manualImplementation(instructions, binding, selector)
}