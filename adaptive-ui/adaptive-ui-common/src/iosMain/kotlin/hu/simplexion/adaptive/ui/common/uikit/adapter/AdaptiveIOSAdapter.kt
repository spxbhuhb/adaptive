/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.uikit.adapter

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.uikit.fragment.UiKitFragmentFactory
import hu.simplexion.adaptive.ui.common.instruction.BoundingRect
import hu.simplexion.adaptive.utility.vmNowMicro
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import platform.UIKit.UIScreen
import platform.UIKit.UIView

open class AdaptiveIOSAdapter(
    override val rootContainer: UIView,
    override val trace: Boolean = false
) : AdaptiveAdapter {

    override val fragmentFactory = UiKitFragmentFactory

    var nextId = 1L

    override val startedAt = vmNowMicro()

    override lateinit var rootFragment: AdaptiveFragment

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    @OptIn(ExperimentalForeignApi::class)
    override fun addActual(fragment: AdaptiveFragment, anchor : AdaptiveFragment?) {
        if (trace) trace(fragment, "before-adapter-addActual", "")

        if (fragment is IOSLayoutFragment) {
            val frame = UIScreen.mainScreen().bounds.useContents {
                BoundingRect(
                    origin.x.toFloat(),
                    origin.y.toFloat(),
                    size.width.toFloat(),
                    size.height.toFloat()
                )
            }

            fragment.setFrame(frame)

            rootContainer.addSubview(fragment.receiver)
        } else {
            if (trace) trace(fragment, "warning-adapter-addActual", "not an IOSLayoutFragment")
        }

        if (trace) trace(fragment, "after-adapter-addActual", "")
        // TODO check if the fragment is root, throw exc otherwise
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        if (trace) trace(fragment, "before-adapter-removeActual", "")

        if (fragment is IOSLayoutFragment) {
            fragment.receiver.removeFromSuperview()
        } else {
            if (trace) trace(fragment, "warning-adapter-removeActual", "not an IOSLayoutFragment")
        }

        if (trace) trace(fragment, "after-adapter-removeActual", "")
        // TODO check if the fragment is root, throw exc otherwise
    }

    override fun newId(): Long =
        nextId ++

}