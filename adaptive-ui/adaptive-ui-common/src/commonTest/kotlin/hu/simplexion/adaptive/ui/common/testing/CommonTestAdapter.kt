/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.testing

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.instruction.Name
import hu.simplexion.adaptive.foundation.query.first
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.fragment.layout.AbstractContainer
import hu.simplexion.adaptive.ui.common.instruction.DPixel
import hu.simplexion.adaptive.ui.common.instruction.SPixel
import hu.simplexion.adaptive.ui.common.platform.MediaMetrics
import hu.simplexion.adaptive.ui.common.support.navigation.AbstractNavSupport
import hu.simplexion.adaptive.utility.alsoIfInstance
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.test.assertEquals

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
                it.computeLayout(tf !!.width, tf.height)
                it.placeLayout(0.0, 0.0)
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
        val data = fragment.renderData

        fragment.receiver.testTop = data.finalTop
        fragment.receiver.testLeft = data.finalLeft
        fragment.receiver.testWidth = data.finalHeight
        fragment.receiver.testHeight = data.finalWidth
    }

    override fun applyRenderInstructions(fragment: AbstractCommonFragment<TestReceiver>) {
        with(fragment) {
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

    fun assertFinal(
        selector: AdaptiveInstruction,
        top: Int,
        left: Int,
        width: Int,
        height: Int
    ) {
        val renderData = (first(true) { selector in it.instructions } as AbstractCommonFragment<*>).renderData

        assertEquals(
            AssertFrame(
                if (selector is Name) selector.name else selector::class.simpleName,
                top.toDouble(),
                left.toDouble(),
                width.toDouble(),
                height.toDouble()
            ),
            AssertFrame(
                if (selector is Name) selector.name else selector::class.simpleName,
                renderData.finalTop,
                renderData.finalLeft,
                renderData.finalWidth,
                renderData.finalHeight
            )
        )
    }

    data class AssertFrame(
        val selector: String?,
        val top: Double,
        val left: Double,
        val width: Double,
        val height: Double
    )
}