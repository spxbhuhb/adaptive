/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.android.adapter

import android.content.Context
import android.view.ViewGroup
import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.android.basic.ViewFragmentFactory
import hu.simplexion.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class AdaptiveAndroidAdapter(
    val context: Context,
    override val rootContainer: ViewGroup,
    override val trace: Boolean = false
) : AdaptiveAdapter {

    override val fragmentFactory = ViewFragmentFactory

    var nextId = 1L

    override val startedAt = vmNowMicro()

    override lateinit var rootFragment: AdaptiveFragment

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    override fun addActual(fragment: AdaptiveFragment, anchor: AdaptiveFragment?) {
        if (fragment is AdaptiveAndroidFragment) {
            rootContainer.addView(fragment.receiver)
        }
        // TODO check if the fragment is root, throw exc otherwise
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        if (fragment is AdaptiveAndroidFragment) {
            rootContainer.removeView(fragment.receiver)
        }
        // TODO check if the fragment is root, throw exc otherwise
    }

    override fun newId(): Long =
        nextId ++

}