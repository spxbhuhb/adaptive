/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.adapter

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import platform.UIKit.UIView

abstract class AdaptiveIOSFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionIndex: Int,
    stateSize : Int
) : AdaptiveFragment(adapter, parent, declarationIndex, instructionIndex, stateSize) {

    abstract val receiver : UIView

    // -------------------------------------------------------------------------
    // Fragment overrides
    // -------------------------------------------------------------------------

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun addActual(fragment: AdaptiveFragment, anchor : AdaptiveFragment?) {
        check(fragment is AdaptiveIOSFragment) { "invalid fragment type" } // TODO user ops
        receiver.addSubview(fragment.receiver)
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        check(fragment is AdaptiveIOSFragment) { "invalid fragment type" } // TODO user ops
        fragment.receiver.removeFromSuperview()
    }

    override fun beforeMount() {
        parent?.addActual(this, null) ?: adapter.addActual(this, null)
    }

    override fun afterUnmount() {
        parent?.removeActual(this) ?: adapter.removeActual(this)
    }

}