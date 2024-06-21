/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.fragment

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveExpect
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.manualImplementation

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

@AdaptiveExpect(foundation)
fun slot(name: String, historySize: Int = 0, @Adaptive initialContent: () -> Unit) {
    manualImplementation(name, initialContent)
}