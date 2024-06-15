/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.platform.ContainerView
import hu.simplexion.adaptive.ui.common.platform.GestureTarget
import hu.simplexion.adaptive.ui.common.render.CommonRenderData
import hu.simplexion.adaptive.ui.common.instruction.Color
import hu.simplexion.adaptive.ui.common.instruction.DPixel
import hu.simplexion.adaptive.ui.common.instruction.SPixel
import hu.simplexion.adaptive.ui.common.support.AbstractContainerFragment
import hu.simplexion.adaptive.ui.common.support.RawFrame
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSSelectorFromString
import platform.QuartzCore.CAGradientLayer
import platform.UIKit.*

open class CommonAdapter(
    override val rootContainer: UIView
) : AbstractCommonAdapter<UIView, ContainerView>() {

    override val fragmentFactory = CommonFragmentFactory

    override fun makeContainerReceiver(fragment: AbstractContainerFragment<UIView, ContainerView>): ContainerView =
        ContainerView(fragment)

    override fun makeStructuralReceiver(fragment: AbstractContainerFragment<UIView, ContainerView>): ContainerView =
        ContainerView(fragment)

    @OptIn(ExperimentalForeignApi::class)
    override fun addActualRoot(fragment: AdaptiveFragment) {
        traceAddActual(fragment)

        fragment.ifIsInstanceOrRoot<AbstractContainerFragment<UIView, ContainerView>> {

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

        fragment.ifIsInstanceOrRoot<AbstractContainerFragment<UIView, UIView>> {
            it.receiver.removeFromSuperview()
        }
    }

    override fun addActual(containerReceiver: ContainerView, itemReceiver: UIView) {
        containerReceiver.addSubview(itemReceiver)
    }

    override fun removeActual(itemReceiver: UIView) {
        itemReceiver.removeFromSuperview()
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun applyLayoutToActual(fragment: AbstractCommonFragment<UIView>) {
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

            view.layer.insertSublayer(gradientLayer, 0u)
            view.layer.cornerRadius = borderRadius.px
            view.layer.masksToBounds = true
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun applyRenderInstructions(fragment: AbstractCommonFragment<UIView>) {
        with(fragment) {
            renderData = CommonRenderData(instructions)
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

            val onClick = renderData.onClick
            if (onClick != null) {
                if (view.gestureRecognizers?.isEmpty() == true) {
                    // FIXME for some reason this point is reached twice
                    view.addGestureRecognizer(UITapGestureRecognizer(GestureTarget(fragment, onClick), NSSelectorFromString("viewTapped")))
                    view.setUserInteractionEnabled(true)
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