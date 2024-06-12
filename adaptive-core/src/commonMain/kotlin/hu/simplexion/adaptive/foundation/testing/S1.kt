/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName", "unused")

package hu.simplexion.adaptive.foundation.testing

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.utility.checkIfInstance

@AdaptiveExpect("test")
fun S1(supportFun: (i: Int) -> Unit) {
    manualImplementation(supportFun)
}

@AdaptiveActual("test:T1")
class AdaptiveS1(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveTestFragment(adapter, parent, index, 1) {

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal(): Boolean = true

    var s0: ((Int) -> Unit)
        get() = state[0].checkIfInstance()
        set(v) {
            state[0] = v
        }

}