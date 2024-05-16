/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.android.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveBridge
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.android.basic.BasicRegistry
import hu.simplexion.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class AdaptiveViewAdapter(
    val context: Context,
    rootView : ViewGroup,
    override val trace : Boolean = false
) : AdaptiveAdapter<View> {

    override val fragmentImplRegistry = BasicRegistry

    var nextId = 1L

    override val startedAt = vmNowMicro()

    override lateinit var rootFragment: AdaptiveFragment<View>

    override val rootBridge = AdaptiveViewPlaceholder(rootView)

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    override fun createPlaceholder(): AdaptiveBridge<View> {
        return AdaptiveViewPlaceholder(ViewStub(context))
    }

    override fun newId(): Long =
        nextId++

}