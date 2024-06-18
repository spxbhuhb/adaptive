/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation.testing

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment

abstract class AdaptiveTestFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int,
    stateSize : Int
) : AdaptiveFragment(adapter, parent, index, -1, stateSize) {

    val receiver = TestNode()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? =
        null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal(): Boolean = true

    override fun addActual(fragment: AdaptiveFragment, direct: Boolean?) {
        // test fragments have no actual UI
    }

    override fun removeActual(fragment: AdaptiveFragment, direct: Boolean?) {
        // test fragments have no actual UI
    }

}