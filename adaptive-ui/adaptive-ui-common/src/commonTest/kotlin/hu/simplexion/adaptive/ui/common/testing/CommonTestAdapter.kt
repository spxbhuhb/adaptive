/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.testing

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.instruction.DPixel
import hu.simplexion.adaptive.ui.common.instruction.SPixel
import hu.simplexion.adaptive.ui.common.platform.MediaMetrics
import hu.simplexion.adaptive.ui.common.support.layout.AbstractContainer
import hu.simplexion.adaptive.ui.common.support.navigation.AbstractNavSupport
import hu.simplexion.adaptive.utility.alsoIfInstance
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class CommonTestAdapter(
    override val rootContainer: TestReceiver = TestReceiver(),
) : AbstractCommonAdapter<TestReceiver, TestReceiver>() {

    override val fragmentFactory = TestFragmentFactory

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override fun makeContainerReceiver(fragment: AbstractContainer<TestReceiver, TestReceiver>): TestReceiver =
        TestReceiver()

    override fun makeStructuralReceiver(fragment: AbstractContainer<TestReceiver, TestReceiver>): TestReceiver =
        TestReceiver()

    override fun addActualRoot(fragment: AdaptiveFragment) {
        traceAddActual(fragment)

        fragment.alsoIfInstance<AbstractContainer<TestReceiver, TestReceiver>> {
            rootContainer.testFrame.let { tf ->
                it.layoutFrameOrNull = tf
                it.measure()
                it.layout(tf)
                rootContainer.children += it.receiver
            }
        }
    }

    override fun removeActualRoot(fragment: AdaptiveFragment) {
        traceRemoveActual(fragment)

        fragment.alsoIfInstance<AbstractContainer<TestReceiver, TestReceiver>> {
            rootContainer.children.remove(it.receiver)
        }
    }

    override fun addActual(containerReceiver: TestReceiver, itemReceiver: TestReceiver) {
        containerReceiver.children += itemReceiver
    }

    override fun removeActual(itemReceiver: TestReceiver) {
        rootContainer.children.remove(itemReceiver)
    }

    override fun applyLayoutToActual(fragment: AbstractCommonFragment<TestReceiver>) {
        val layoutFrame = fragment.layoutFrameOrNull

        if (layoutFrame != null) {
            fragment.receiver.testTop = layoutFrame.top
            fragment.receiver.testLeft = layoutFrame.left
            fragment.receiver.testWidth = layoutFrame.width
            fragment.receiver.testHeight = layoutFrame.height
        }
    }

    override fun applyRenderInstructions(fragment: AbstractCommonFragment<TestReceiver>) {
        with (fragment) {
            if (renderData.tracePatterns.isNotEmpty()) {
                tracePatterns = renderData.tracePatterns
            }
        }
    }

    override fun openExternalLink(href: String) {

    }

    override fun toPx(dPixel: DPixel): Double =
        dPixel.value

    override fun toPx(sPixel: SPixel): Double =
        sPixel.value

    override var mediaMetrics: MediaMetrics
        get() = TODO("Not yet implemented")
        set(value) {}

    override val navSupport: AbstractNavSupport
        get() = TODO("Not yet implemented")
}