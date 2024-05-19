/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.android.adapter

import android.view.View
import hu.simplexion.adaptive.foundation.*
import java.lang.UnsupportedOperationException

abstract class AdaptiveViewFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int,
    stateSize : Int
) : AdaptiveFragment(adapter, parent, index, -1, stateSize) {

    abstract val receiver : View

    val viewAdapter
        get() = adapter as AdaptiveViewAdapter

    // -------------------------------------------------------------------------
    // Fragment overrides
    // -------------------------------------------------------------------------

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun addActual(fragment: AdaptiveFragment) {
        throw UnsupportedOperationException() // TODO ops instead of Unsupported
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        throw UnsupportedOperationException() // TODO ops instead of Unsupported
    }
}