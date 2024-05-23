/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android.adapter

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.WindowManager
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.opsCheck
import hu.simplexion.adaptive.ui.common.android.fragment.ViewFragmentFactory
import hu.simplexion.adaptive.ui.common.fragment.AdaptiveUIAdapter
import hu.simplexion.adaptive.ui.common.instruction.BoundingRect

open class AdaptiveAndroidAdapter(
    val context: Context,
    override val rootContainer: ViewGroup,
    override val trace: Boolean = false
) : AdaptiveUIAdapter() {

    override val fragmentFactory = ViewFragmentFactory

    override fun addActual(fragment: AdaptiveFragment) {
        traceAddActual(fragment)

        fragment.ifIsInstanceOrRoot<AndroidLayoutFragment> {
            it.frame = BoundingRect(0f, 0f, rootContainer.width.toFloat(), rootContainer.height.toFloat())
            rootContainer.addView(it.receiver)
        }

    }

    override fun removeActual(fragment: AdaptiveFragment) {
        traceRemoveActual(fragment)

        fragment.ifIsInstanceOrRoot<AndroidLayoutFragment> {
            rootContainer.removeView(it.receiver)
        }

    }

}