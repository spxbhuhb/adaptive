/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName")

package hu.simplexion.adaptive.base.testing

import hu.simplexion.adaptive.base.Adaptive
import hu.simplexion.adaptive.base.AdaptiveAdapter
import hu.simplexion.adaptive.base.AdaptiveFragment
import hu.simplexion.adaptive.base.manualImplementation

@Adaptive
@Suppress("unused", "FunctionName")
fun T1(p0: Int) {
    manualImplementation(p0)
}

class AdaptiveT1<BT>(
    adapter: AdaptiveAdapter<BT>,
    parent: AdaptiveFragment<BT>?,
    index: Int
) : AdaptiveFragment<BT>(adapter, parent, index, 1) {

    override fun genBuild(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT>? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment<BT>) = Unit

    override fun genPatchInternal() = Unit

}