/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.testing.adapter

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIAdapter
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIContainerFragment
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.testing.fragment.TestFragmentFactory
import hu.simplexion.adaptive.utility.alsoIfInstance
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class AdaptiveUITestAdapter(
    override val rootContainer: TestReceiver
) : AdaptiveUIAdapter<TestReceiver, TestReceiver>() {

    override val fragmentFactory = TestFragmentFactory

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override fun makeContainerReceiver(fragment: AdaptiveUIContainerFragment<TestReceiver, TestReceiver>): TestReceiver =
        TestReceiver()

    override fun addActualRoot(fragment: AdaptiveFragment) {
        traceAddActual(fragment)

        fragment.alsoIfInstance<AdaptiveUIContainerFragment<TestReceiver, TestReceiver>> {
            rootContainer.testFrame.let { tf ->
                it.renderData.layoutFrame = tf
                it.measure()
                it.layout(tf)
                rootContainer.children += it.receiver
            }
        }
    }

    override fun removeActualRoot(fragment: AdaptiveFragment) {
        traceRemoveActual(fragment)

        fragment.alsoIfInstance<AdaptiveUIContainerFragment<TestReceiver, TestReceiver>> {
            rootContainer.children.remove(it.receiver)
        }
    }

    override fun addActual(containerReceiver: TestReceiver, itemReceiver: TestReceiver) {
        containerReceiver.children += itemReceiver
    }

    override fun removeActual(itemReceiver: TestReceiver) {
        rootContainer.children.remove(itemReceiver)
    }

    override fun actualLayout(fragment: AdaptiveUIFragment<TestReceiver>, proposedFrame: Frame) {
        fragment.setLayoutFrame(proposedFrame)

        val layoutFrame = fragment.renderData.layoutFrame

        if (layoutFrame !== Frame.NaF) {
            val point = layoutFrame.point
            val size = layoutFrame.size

            fragment.receiver.testTop = point.top
            fragment.receiver.testLeft = point.left
            fragment.receiver.testWidth = size.width
            fragment.receiver.testHeight = size.height
        }
    }

    override fun applyRenderInstructions(fragment: AdaptiveUIFragment<TestReceiver>) {
        with (fragment) {
            if (renderData.tracePatterns.isNotEmpty()) {
                tracePatterns = renderData.tracePatterns
            }
        }
    }

    override fun openExternalLink(href: String) {

    }
}