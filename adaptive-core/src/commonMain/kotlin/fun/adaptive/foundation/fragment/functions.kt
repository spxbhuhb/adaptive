/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation.fragment

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.manualImplementation

@AdaptiveExpect(foundation)
fun delegate(
    buildFun: (AdaptiveFragment.(parent: AdaptiveFragment, declarationIndex: Int) -> AdaptiveFragment?)? = null,
    patchDescendantFun: (AdaptiveFragment.(fragment: AdaptiveFragment) -> Unit)? = null,
    patchInternalFun: (AdaptiveFragment.() -> Boolean)? = null
) {
    manualImplementation(buildFun, patchDescendantFun, patchInternalFun)
}

@AdaptiveExpect(foundation)
fun measureFragmentTime(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit) {
    manualImplementation(instructions, content)
}