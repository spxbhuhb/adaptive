/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.testing

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.manualImplementation

@Adaptive
@Suppress("FunctionName")
fun T0() {
    manualImplementation()
}

@Suppress("unused")
class AdaptiveT0<BT>(
    adapter: AdaptiveAdapter<BT>,
    parent: AdaptiveFragment<BT>?,
    index: Int
) : AdaptiveFragment<BT>(adapter, parent, index, 0) {

    override fun genBuild(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT>? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment<BT>) = Unit

    override fun genPatchInternal() = Unit

}