/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.android.adapter

import android.view.ViewGroup
import hu.simplexion.adaptive.foundation.*

abstract class AdaptiveAndroidGroupFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int,
    stateSize : Int
) : AdaptiveAndroidFragment(adapter, parent, index, stateSize) {

    abstract override val receiver : ViewGroup

    // -------------------------------------------------------------------------
    // Fragment overrides
    // -------------------------------------------------------------------------

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun addActual(fragment: AdaptiveFragment, anchor : AdaptiveFragment?) {
        check(fragment is AdaptiveAndroidFragment) { "invalid fragment type" } // TODO user ops
        receiver.addView(fragment.receiver)
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        check(fragment is AdaptiveAndroidFragment) { "invalid fragment type" } // TODO user ops
        receiver.removeView(fragment.receiver)
    }
}