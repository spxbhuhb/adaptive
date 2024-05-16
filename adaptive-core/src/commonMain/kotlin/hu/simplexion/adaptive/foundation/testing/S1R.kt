/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName")

package hu.simplexion.adaptive.foundation.testing

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.internal.BoundSupportFunction

@Adaptive
@Suppress("unused", "FunctionName")
fun S1R(supportFun: (i: Int) -> Int) {
    manualImplementation(supportFun)
}

@Suppress("unused")
class AdaptiveS1R<BT>(
    adapter: AdaptiveAdapter<BT>,
    parent: AdaptiveFragment<BT>?,
    index: Int
) : AdaptiveFragment<BT>(adapter, parent, index, 1) {

    override fun genBuild(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT>? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment<BT>) = Unit

    override fun genPatchInternal() = Unit

    var s0: BoundSupportFunction
        get() = state[0] as BoundSupportFunction
        set(v) {
            state[0] = v
        }

}