/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.testing

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.Name
import `fun`.adaptive.foundation.query.first
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.SPixel
import `fun`.adaptive.ui.platform.media.MediaMetrics
import `fun`.adaptive.ui.testing.platform.NavSupport
import `fun`.adaptive.utility.alsoIfInstance
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.test.assertEquals

class AuiTestAdapter(
    override val rootContainer: TestReceiver = TestReceiver(),
    override val transport: ServiceCallTransport = TestServiceTransport()
) : AbstractAuiAdapter<TestReceiver, TestReceiver>() {

    override val fragmentFactory = AuiFragmentFactory

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

    override fun applyLayoutToActual(fragment: AbstractAuiFragment<TestReceiver>) {
        val data = fragment.renderData

        fragment.receiver.testTop = data.finalTop
        fragment.receiver.testLeft = data.finalLeft
        fragment.receiver.testWidth = data.finalHeight
        fragment.receiver.testHeight = data.finalWidth
    }

    override fun applyLayoutIndependent(fragment: AbstractAuiFragment<TestReceiver>) {
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

    override fun toDp(value: Double): DPixel =
        DPixel(value)

    override fun toPx(sPixel: SPixel): Double =
        sPixel.value

    override var mediaMetrics: MediaMetrics
        get() = TODO("Not yet implemented")
        set(value) {}

    override val navSupport = NavSupport(this)

    operator fun get(selector: AdaptiveInstruction) =
        first(true) { selector in it.instructions } as AbstractAuiFragment<*>

    fun assertFinal(
        selector: AdaptiveInstruction,
        frame: RawFrame
    ) {
        val renderData = (first(true) { selector in it.instructions } as AbstractAuiFragment<*>).renderData

        assertEquals(
            AssertFrame(
                if (selector is Name) selector.name else selector::class.simpleName,
                frame.top,
                frame.left,
                frame.width,
                frame.height
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

    fun assertFinal(
        selector: AdaptiveInstruction,
        top: Int,
        left: Int,
        width: Int,
        height: Int
    ) {
        val renderData = (first(true) { selector in it.instructions } as AbstractAuiFragment<*>).renderData

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