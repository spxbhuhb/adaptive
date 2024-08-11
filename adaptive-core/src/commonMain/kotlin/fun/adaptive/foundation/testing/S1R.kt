/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName", "unused")

package `fun`.adaptive.foundation.testing

import `fun`.adaptive.foundation.*

import `fun`.adaptive.utility.checkIfInstance

@AdaptiveExpect("test")
fun S1R(supportFun: (i: Int) -> Int) {
    manualImplementation(supportFun)
}

@AdaptiveActual("test")
class AdaptiveS1R(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveTestFragment(adapter, parent, index, 1) {

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal(): Boolean = true

    var s0: ((Int) -> Int)
        get() = state[0].checkIfInstance()
        set(v) {
            state[0] = v
        }

}