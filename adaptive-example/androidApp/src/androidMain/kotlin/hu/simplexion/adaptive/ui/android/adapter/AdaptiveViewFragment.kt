/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.android.adapter

import android.view.View
import android.view.ViewGroup
import hu.simplexion.adaptive.base.*

abstract class AdaptiveViewFragment(
    adapter: AdaptiveAdapter<View>,
    parent: AdaptiveFragment<View>?,
    index: Int,
    stateSize : Int,
    val leaf : Boolean
) : AdaptiveFragment<View>(adapter, parent, index, stateSize), AdaptiveBridge<View> {

    val viewAdapter
        get() = adapter as AdaptiveViewAdapter

    // -------------------------------------------------------------------------
    // Bridge overrides
    // -------------------------------------------------------------------------

    override fun remove(child: AdaptiveBridge<View>) {
        check(!leaf)
        (receiver as ViewGroup).removeView(child.receiver)
    }

    override fun replace(oldChild: AdaptiveBridge<View>, newChild: AdaptiveBridge<View>) {
        check(!leaf)
        throw UnsupportedOperationException()
    }

    override fun add(child: AdaptiveBridge<View>) {
        check(!leaf)
        (receiver as ViewGroup).addView(child.receiver)
    }

    // -------------------------------------------------------------------------
    // Fragment overrides
    // -------------------------------------------------------------------------

    override fun genBuild(parent: AdaptiveFragment<View>, declarationIndex: Int): AdaptiveFragment<View>? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment<View>) = Unit

    override fun innerMount(bridge: AdaptiveBridge<View>) {
        bridge.add(this)
        containedFragment?.mount(this)
    }

    override fun innerUnmount(bridge: AdaptiveBridge<View>) {
        containedFragment?.unmount(this)
        bridge.remove(this)
    }

}