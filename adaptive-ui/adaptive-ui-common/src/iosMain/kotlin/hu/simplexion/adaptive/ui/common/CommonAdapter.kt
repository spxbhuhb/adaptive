/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.resource.defaultResourceEnvironment
import hu.simplexion.adaptive.ui.common.instruction.Color
import hu.simplexion.adaptive.ui.common.instruction.DPixel
import hu.simplexion.adaptive.ui.common.instruction.SPixel
import hu.simplexion.adaptive.ui.common.platform.ContainerView
import hu.simplexion.adaptive.ui.common.platform.GestureTarget
import hu.simplexion.adaptive.ui.common.platform.MediaMetrics
import hu.simplexion.adaptive.ui.common.render.DecorationRenderData
import hu.simplexion.adaptive.ui.common.render.EventRenderData
import hu.simplexion.adaptive.ui.common.render.LayoutRenderData
import hu.simplexion.adaptive.ui.common.render.TextRenderData
import hu.simplexion.adaptive.ui.common.support.layout.AbstractContainer
import hu.simplexion.adaptive.ui.common.support.layout.RawFrame
import hu.simplexion.adaptive.ui.common.support.navigation.AbstractNavSupport
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSSelectorFromString
import platform.QuartzCore.CAGradientLayer
import platform.UIKit.*

open class CommonAdapter(
    final override val rootContainer: UIView
) : AbstractCommonAdapter<UIView, ContainerView>() {

    override val fragmentFactory = CommonFragmentFactory

    override fun makeContainerReceiver(fragment: AbstractContainer<UIView, ContainerView>): ContainerView =
        ContainerView(fragment)

    override fun makeStructuralReceiver(fragment: AbstractContainer<UIView, ContainerView>): ContainerView =
        ContainerView(fragment)

    @OptIn(ExperimentalForeignApi::class)
    override fun addActualRoot(fragment: AdaptiveFragment) {
        traceAddActual(fragment)

        fragment.ifIsInstanceOrRoot<AbstractContainer<UIView, ContainerView>> {

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

        fragment.ifIsInstanceOrRoot<AbstractContainer<UIView, UIView>> {
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

        val view = fragment.receiver
        val frame = CGRectMake(layoutFrame.left, layoutFrame.top, layoutFrame.width, layoutFrame.height)

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

        renderData.decoration { it.apply(receiver, renderData.layout) }
        renderData.text { it.apply(receiver) }
        renderData.event { it.apply(receiver, fragment) }
    }

    @OptIn(ExperimentalForeignApi::class)
    fun DecorationRenderData.apply(view: UIView, layoutRenderData: LayoutRenderData?) {

        backgroundColor { view.backgroundColor = it.uiColor }

        val borderWidth = layoutRenderData?.border?.top // FIXME individual border widths
        val borderColor = this.borderColor

        if (borderWidth != null && borderColor != null) {
            view.layer.borderWidth = borderWidth
            view.layer.borderColor = borderColor.uiColor.CGColor()
            view.layer.masksToBounds = true

            cornerRadius { br -> view.layer.cornerRadius = br.topLeft } // FIXME individual radii for corners
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

            cornerRadius { br -> view.layer.cornerRadius = br.topLeft } // FIXME individual radii for corners
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

    @OptIn(ExperimentalForeignApi::class)
    override var mediaMetrics = rootContainer.frame.useContents {
        MediaMetrics(size.width, size.height, defaultResourceEnvironment.theme)
    }

    override val navSupport: AbstractNavSupport
        get() = TODO("Not yet implemented")
}