/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.uikit

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AdaptiveUIAdapter
import hu.simplexion.adaptive.ui.common.AdaptiveUIContainerFragment
import hu.simplexion.adaptive.ui.common.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.RenderData
import hu.simplexion.adaptive.ui.common.instruction.Color
import hu.simplexion.adaptive.ui.common.instruction.DPixel
import hu.simplexion.adaptive.ui.common.instruction.SPixel
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.uikit.fragment.UiKitFragmentFactory
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.QuartzCore.CAGradientLayer
import platform.UIKit.*

open class AdaptiveIosAdapter(
    override val rootContainer: UIView
) : AdaptiveUIAdapter<UIView, UIView>() {

    override val fragmentFactory = UiKitFragmentFactory

    override fun makeContainerReceiver(fragment: AdaptiveUIContainerFragment<UIView, UIView>): UIView =
        UIView()

    override fun makeAnchorReceiver(containerFragment: AdaptiveUIContainerFragment<UIView, UIView>): UIView =
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

        val view = fragment.receiver
        val frame = CGRectMake(left.toDouble(), top.toDouble(), size.width.toDouble(), size.height.toDouble())
        val renderData = fragment.renderData

        view.setFrame(frame)

        val backgroundColor = renderData.backgroundColor

        if (backgroundColor != null) {
            view.backgroundColor = backgroundColor.uiColor
        }

        val border = renderData.border
        val borderRadius = renderData.borderRadius

        if (border != null) {
            view.layer.borderWidth = border.width.px
            view.layer.borderColor = border.color.uiColor.CGColor()
            view.layer.masksToBounds = true
        }

        if (borderRadius != null) {
            view.layer.cornerRadius = borderRadius.px
        }

        val backgroundGradient = renderData.backgroundGradient

        if (backgroundGradient != null) {
            val gradientLayer = CAGradientLayer()
            gradientLayer.frame = frame
            gradientLayer.colors = listOf(backgroundGradient.start.uiColor, backgroundGradient.end.uiColor)

            val rectangle = UIView(frame)
            rectangle.layer.insertSublayer(gradientLayer, 0u)
            rectangle.layer.cornerRadius = borderRadius.px
            rectangle.layer.masksToBounds = true
        }
    }

    override fun applyRenderInstructions(fragment: AdaptiveUIFragment<UIView>) {
        with(fragment) {
            renderData = RenderData(instructions)
            // FIXME should clear actual UI settings when null

            if (renderData.tracePatterns.isNotEmpty()) {
                tracePatterns = renderData.tracePatterns
            }

            val view = receiver

            with(renderData) {

                if (view is UILabel) {
                    color?.let { view.setTextColor(it.uiColor) }

                    val fontSize = this.fontSize
                    val fontWeight = this.fontWeight

                    // FIXME font handling in iOS
                    view.font = when {
                        fontSize != null && fontWeight != null -> UIFont.boldSystemFontOfSize(toPx(fontSize).toDouble())
                        fontSize != null -> UIFont.systemFontOfSize(toPx(fontSize).toDouble())
                        else -> UIFont.boldSystemFontOfSize(UIFont.systemFontSize)
                    }

                    // FIXME text attributes in iOS
//                    letterSpacing?.let { view.letterSpacing = it }
//
//                    when (textAlign) {
//                        TextAlign.Start -> view.textAlignment = TEXT_ALIGNMENT_VIEW_START
//                        TextAlign.Center -> view.textAlignment = TEXT_ALIGNMENT_CENTER
//                        TextAlign.End -> view.textAlignment = TEXT_ALIGNMENT_VIEW_END
//                        null -> Unit
//                    }
                }
            }
        }
    }

    override fun toPx(dPixel: DPixel): Float {
        // FIXME dpixel to pixel for iOS
        return dPixel.value
    }

    override fun toPx(sPixel: SPixel): Float {
        // FIXME DPixel to pixel for iOS
        return sPixel.value
    }

    val DPixel?.px: Double
        inline get() = this?.value?.toDouble() ?: 0.0

    val Color.uiColor: UIColor
        get() {
            // TODO val alpha = ((value shr 24) and 0xFF) / 255.0
            val red = ((value shr 16) and 0xFF) / 255.0
            val green = ((value shr 8) and 0xFF) / 255.0
            val blue = (value and 0xFF) / 255.0

            return UIColor.colorWithRed(red, green, blue, 1.0)
        }
}