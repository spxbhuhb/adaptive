/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.platform.ContainerView
import hu.simplexion.adaptive.ui.common.platform.GestureTarget
import hu.simplexion.adaptive.ui.common.render.CommonRenderData
import hu.simplexion.adaptive.ui.common.render.DecorationRenderData
import hu.simplexion.adaptive.ui.common.render.EventRenderData
import hu.simplexion.adaptive.ui.common.render.TextRenderData
import hu.simplexion.adaptive.ui.common.support.AbstractContainerFragment
import hu.simplexion.adaptive.ui.common.support.RawFrame
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGPointMake
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
                RawFrame(origin.y, origin.x, size.width, size.height)
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
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun applyRenderInstructions(fragment: AbstractCommonFragment<UIView>) {
        val renderData = fragment.renderData
        val receiver = fragment.receiver

        // FIXME should clear actual UI settings when null

        if (renderData.tracePatterns.isNotEmpty()) {
            fragment.tracePatterns = renderData.tracePatterns
        }

        renderData.decoration { it.apply(receiver) }
        renderData.text { it.apply(receiver) }
        renderData.event { it.apply(receiver, fragment) }
    }

    @OptIn(ExperimentalForeignApi::class)
    fun DecorationRenderData.apply(view: UIView) {

        backgroundColor { view.backgroundColor = it.uiColor }

        border {
            view.layer.borderWidth = it.width.px
            view.layer.borderColor = it.color.uiColor.CGColor()
            view.layer.masksToBounds = true

            borderRadius { br -> view.layer.cornerRadius = br.topLeft.px } // FIXME individual radii for corners
        }

        // FIXME individual radii for corners

        backgroundGradient {
            val gradientLayer = CAGradientLayer()
            gradientLayer.frame = view.layer.frame
            gradientLayer.colors = listOf(it.start.uiColor, it.end.uiColor)
            gradientLayer.startPoint = it.startPosition.let { p -> CGPointMake(p.left, p.top) }
            gradientLayer.endPoint = it.endPosition.let { p -> CGPointMake(p.left, p.top) }

            view.layer.insertSublayer(gradientLayer, 0u)
            view.layer.masksToBounds = true

            borderRadius { br -> view.layer.cornerRadius = br.topLeft.px } // FIXME individual radii for corners
        }
    }

    fun TextRenderData.apply(view: UIView) {
        if (view !is UILabel) return

        color { view.setTextColor(it.uiColor) }

        val fontSize = this.fontSize
        val fontWeight = this.fontWeight

        // FIXME font handling in iOS
        view.font = when {
            fontSize != null && fontWeight != null -> UIFont.boldSystemFontOfSize(toPx(fontSize))
            fontSize != null -> UIFont.systemFontOfSize(toPx(fontSize))
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

    @OptIn(ExperimentalForeignApi::class)
    fun EventRenderData.apply(view : UIView, fragment: AbstractCommonFragment<UIView>) {
        onClickListener {
            it as UITapGestureRecognizer
            view.removeGestureRecognizer(it)
            view.setUserInteractionEnabled(false)
        }

        onClick { oc ->
            UITapGestureRecognizer(GestureTarget(fragment, oc), NSSelectorFromString("viewTapped")).also {
                onClickListener = it
                view.addGestureRecognizer(it)
                view.setUserInteractionEnabled(true)
            }
        }
    }

    inline operator fun <reified T : Any> T?.invoke(function: (it: T) -> Unit) {
        if (this != null) {
            function(this)
        }
    }


    override fun toPx(dPixel: DPixel): Double {
        // FIXME dpixel to pixel for iOS
        return dPixel.value
    }

    override fun toPx(sPixel: SPixel): Double {
        // FIXME DPixel to pixel for iOS
        return sPixel.value
    }

    val DPixel?.px: Double
        inline get() = this?.value ?: 0.0

    val Color.uiColor: UIColor
        get() {
            // TODO val alpha = ((value shr 24) and 0xFF) / 255.0
            val red = ((value shr 16) and 0xFFu).toInt() / 255.0
            val green = ((value shr 8) and 0xFFu).toInt() / 255.0
            val blue = (value and 0xFFu).toInt() / 255.0

            return UIColor.colorWithRed(red, green, blue, 1.0)
        }
}