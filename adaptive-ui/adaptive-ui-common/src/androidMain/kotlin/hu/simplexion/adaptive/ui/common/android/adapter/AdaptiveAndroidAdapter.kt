/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android.adapter

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.WindowManager
import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.android.fragment.ViewFragmentFactory
import hu.simplexion.adaptive.ui.common.instruction.BoundingRect
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

        val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)

        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        if (fragment is AndroidLayoutFragment) {
            fragment.setFrame(BoundingRect(0f, 0f, screenWidth.toFloat(), screenHeight.toFloat()))
            rootContainer.addView(fragment.receiver)
        }
        // TODO check if the fragment is root, throw exc otherwise
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        if (fragment is AndroidLayoutFragment) {
            rootContainer.removeView(fragment.receiver)
        }
        // TODO check if the fragment is root, throw exc otherwise
    }

    override fun newId(): Long =
        nextId ++

}