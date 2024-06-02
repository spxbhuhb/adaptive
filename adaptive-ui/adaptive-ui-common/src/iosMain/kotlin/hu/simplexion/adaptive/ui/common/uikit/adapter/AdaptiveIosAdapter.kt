/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.uikit.adapter

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIAdapter
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIContainerFragment
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.adapter.RenderData
import hu.simplexion.adaptive.ui.common.instruction.DPixel
import hu.simplexion.adaptive.ui.common.instruction.SPixel
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.uikit.fragment.UiKitFragmentFactory
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIView

open class AdaptiveIosAdapter(
    override val rootContainer: UIView
) : AdaptiveUIAdapter<UIView, UIView>() {

    override val fragmentFactory = UiKitFragmentFactory

    override fun makeContainerReceiver(fragment: AdaptiveUIContainerFragment<UIView, UIView>): UIView =
        UIView()

    @OptIn(ExperimentalForeignApi::class)
    override fun addActualRoot(fragment: AdaptiveFragment) {
        traceAddActual(fragment)

        fragment.ifIsInstanceOrRoot<AdaptiveUIContainerFragment<UIView, UIView>> {

            val frame = rootContainer.frame.useContents {
                RawFrame(
                    origin.y.toFloat(),
                    origin.x.toFloat(),
                    size.width.toFloat(),
                    size.height.toFloat()
                )
            }

            it.layoutFrame = frame
            it.measure()
            it.layout(frame)

            rootContainer.addSubview(it.receiver)
        }
    }

    override fun removeActualRoot(fragment: AdaptiveFragment) {
        traceRemoveActual(fragment)

        fragment.ifIsInstanceOrRoot<AdaptiveUIContainerFragment<UIView, UIView>> {
            it.receiver.removeFromSuperview()
        }
    }

    override fun addActual(containerReceiver: UIView, itemReceiver: UIView) {
        containerReceiver.addSubview(itemReceiver)
    }

    override fun removeActual(itemReceiver: UIView) {
        itemReceiver.removeFromSuperview()
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun applyLayoutToActual(fragment: AdaptiveUIFragment<UIView>) {
        val layoutFrame = fragment.layoutFrame

        val point = layoutFrame.point
        val size = layoutFrame.size
        val top = point.top.toInt()
        val left = point.left.toInt()

        fragment.receiver.setFrame(
            CGRectMake(left.toDouble(), top.toDouble(), size.width.toDouble(), size.height.toDouble())
        )
    }

    override fun applyRenderInstructions(fragment: AdaptiveUIFragment<UIView>) {
        with(fragment) {
            renderData = RenderData(instructions)
            // FIXME should clear actual UI settings when null

            if (renderData.tracePatterns.isNotEmpty()) {
                tracePatterns = renderData.tracePatterns
            }

            val view = receiver

            // TODO
        }
    }

    override fun toPx(dPixel: DPixel): Float {
        TODO("Not yet implemented")
    }

    override fun toPx(sPixel: SPixel): Float {
        TODO("Not yet implemented")
    }
}