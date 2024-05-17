/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.android.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.android.basic.ViewFragmentFactory
import hu.simplexion.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class AdaptiveViewAdapter(
    val context: Context,
    override val rootContainer : ViewGroup,
    override val trace : Boolean = false
) : AdaptiveAdapter {

    override val fragmentFactory = ViewFragmentFactory

    var nextId = 1L

    override val startedAt = vmNowMicro()

    override lateinit var rootFragment: AdaptiveFragment

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    override fun addActual(fragment: AdaptiveFragment) {
        check(fragment is AdaptiveViewFragment) { "invalid fragment type" } // TODO user ops
        rootContainer.addView(fragment.receiver)
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        check(fragment is AdaptiveViewFragment) { "invalid fragment type" } // TODO user ops
        rootContainer.removeView(fragment.receiver)
    }

    override fun createPlaceholder(parent : AdaptiveFragment, index : Int): AdaptiveFragment {
        return AdaptiveViewPlaceholder(ViewStub(context), this, parent, index)
    }

    override fun newId(): Long =
        nextId++

}