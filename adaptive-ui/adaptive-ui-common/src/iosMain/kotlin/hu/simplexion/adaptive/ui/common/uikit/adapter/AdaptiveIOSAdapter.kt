/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.uikit.adapter

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIAdapter
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.uikit.fragment.UiKitFragmentFactory
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIView

open class AdaptiveIOSAdapter(
    override val rootContainer: UIView,
    override val trace: Boolean = false
) : AdaptiveUIAdapter() {

    override val fragmentFactory = UiKitFragmentFactory

    @OptIn(ExperimentalForeignApi::class)
    override fun addActual(fragment: AdaptiveFragment) {
        traceAddActual(fragment)

        fragment.ifIsInstanceOrRoot<IOSLayoutFragment> {

            it.frame = rootContainer.frame.useContents {
                Frame(
                    origin.y.toFloat(),
                    origin.x.toFloat(),
                    size.width.toFloat(),
                    size.height.toFloat()
                )
            }

            rootContainer.addSubview(it.receiver)
        }
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        if (trace) trace(fragment, "before-adapter-removeActual", "")

        fragment.ifIsInstanceOrRoot<IOSLayoutFragment> {
            it.receiver.removeFromSuperview()
        }

    }

}