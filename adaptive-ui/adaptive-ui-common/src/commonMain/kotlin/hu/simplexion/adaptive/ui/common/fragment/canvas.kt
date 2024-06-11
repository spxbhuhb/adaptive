/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveExpect
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.manualImplementation
import hu.simplexion.adaptive.resource.DrawableResource
import hu.simplexion.adaptive.ui.common.commonUI

@AdaptiveExpect(commonUI)
fun canvas(vararg instructions : AdaptiveInstruction, @Adaptive content : () -> Unit) {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(commonUI)
fun circle() {

}