/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.android.fragment.ViewFragmentFactory
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIAdapter
import hu.simplexion.adaptive.ui.common.instruction.Frame

open class AdaptiveAndroidAdapter(
    val context: Context,
    override val rootContainer: ViewGroup
) : AdaptiveUIAdapter() {

    override val fragmentFactory = ViewFragmentFactory

    override fun addActual(fragment: AdaptiveFragment) {
        traceAddActual(fragment)

        fragment.ifIsInstanceOrRoot<AndroidLayoutFragment> {
            it.frame = Frame(0f, 0f, rootContainer.width.toFloat(), rootContainer.height.toFloat())
            it.receiver.layoutParams = LinearLayout.LayoutParams(rootContainer.width, rootContainer.height)
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