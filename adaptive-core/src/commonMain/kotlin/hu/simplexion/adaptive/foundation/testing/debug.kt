/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName")

package hu.simplexion.adaptive.foundation.testing

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.manualImplementation

@Adaptive
fun debug(vararg out: Any?) {
    manualImplementation(out)
}

class AdaptiveDebug<BT>(
    adapter: AdaptiveAdapter<BT>,
    parent: AdaptiveFragment<BT>?,
    index: Int
) : AdaptiveFragment<BT>(adapter, parent, index, 1) {

    override fun genBuild(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT>? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment<BT>) = Unit

    override fun genPatchInternal() {
        @Suppress("UNCHECKED_CAST")
        println("debug: " + (state[0] as Array<out Any>).contentToString())
    }
}