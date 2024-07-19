/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.resource.defaultResourceEnvironment
import hu.simplexion.adaptive.ui.common.fragment.layout.AbstractContainer
import hu.simplexion.adaptive.ui.common.fragment.layout.RawSize
import hu.simplexion.adaptive.ui.common.instruction.DPixel
import hu.simplexion.adaptive.ui.common.instruction.SPixel
import hu.simplexion.adaptive.ui.common.platform.ContainerView
import hu.simplexion.adaptive.ui.common.platform.MediaMetrics
import hu.simplexion.adaptive.ui.common.render.applyDecoration
import hu.simplexion.adaptive.ui.common.render.applyText
import hu.simplexion.adaptive.ui.common.support.navigation.AbstractNavSupport
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
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

            val size = rootContainer.frame.useContents {
                RawSize(size.width, size.height)
            }

            it.computeLayout(size.width, size.height)
            it.placeLayout(0.0, 0.0)

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
        val data = fragment.renderData

        val view = fragment.receiver
        val frame = CGRectMake(data.finalLeft, data.finalTop, data.finalWidth, data.finalHeight)

        view.setFrame(frame)
        applyRenderInstructions(fragment)
    }

    override fun applyRenderInstructions(fragment: AbstractCommonFragment<UIView>) {
        val renderData = fragment.renderData

        if (renderData.tracePatterns.isNotEmpty()) {
            fragment.tracePatterns = renderData.tracePatterns
        }

        applyDecoration(fragment)
        applyText(fragment)

        fragment.previousRenderData = fragment.renderData
    }

    override fun toPx(dPixel: DPixel): Double {
        // FIXME dpixel to pixel for iOS
        return dPixel.value
    }

    override fun toDp(value: Double): DPixel {
        TODO("Not yet implemented")
    }

    override fun toPx(sPixel: SPixel): Double {
        // FIXME DPixel to pixel for iOS
        return sPixel.value
    }

    @OptIn(ExperimentalForeignApi::class)
    override var mediaMetrics = rootContainer.frame.useContents {
        MediaMetrics(size.width, size.height, defaultResourceEnvironment.theme, manualTheme)
    }

    override val navSupport: AbstractNavSupport
        get() = TODO("Not yet implemented")
}