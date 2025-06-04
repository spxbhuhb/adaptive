package `fun`.adaptive.ui.fragment.layout.splitpane

import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.fragment.layout.SplitPaneViewBackend
import `fun`.adaptive.ui.fragment.layout.SplitPaneViewBackend.Companion.splitPaneBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.testing.snapshotTest
import kotlin.js.JsName
import kotlin.test.Test

class SplitPaneHeightTest {

    val first = "first"
    val divider = "divider"
    val second = "second"
    val wrapper = "wrapper"
    val wrapped = "wrapped"

    @Test
    @JsName("paneContentSizeHorizontalBothFixFirst")
    fun `pane content size, horizontal, both, fixFirst`() {
        val firstWidth = 100.0
        val paneHeight = 50

        val viewBackend = splitPaneBackend(SplitVisibility.Both, SplitMethod.FixFirst, firstWidth, Orientation.Horizontal)

        val dividerOverlaySize = viewBackend.dividerOverlaySize.value
        val dividerEffectiveSize = viewBackend.dividerEffectiveSize.value
        val dividerShift = (dividerOverlaySize - dividerEffectiveSize) / 2

        snapshotTest {

            splitPane(
                viewBackend,
                { box { name(first) .. maxSize } },
                { box { name(divider) .. maxSize } },
                { box { name(second) .. maxSize } }
            ) .. height { paneHeight.dp }

        }.apply {

            assertLayoutParentFinal(first, 0, 0, firstWidth, paneHeight)
            assertLayoutParentFinal(divider, 0, firstWidth - dividerShift, dividerOverlaySize, paneHeight)
            assertLayoutParentFinal(second, 0, firstWidth + dividerEffectiveSize, testWidth - firstWidth - dividerEffectiveSize, paneHeight)

        }
    }

    @Test
    @JsName("paneContentSizeVerticalBothFixFirst")
    fun `pane content size, vertical, both, fixFirst`() {
        val paneWidth = 50
        val firstHeight = 100.0

        val viewBackend = splitPaneBackend(SplitVisibility.Both, SplitMethod.FixFirst, firstHeight, Orientation.Vertical)

        val dividerOverlaySize = viewBackend.dividerOverlaySize.value
        val dividerEffectiveSize = viewBackend.dividerEffectiveSize.value
        val dividerShift = (dividerOverlaySize - dividerEffectiveSize) / 2

        snapshotTest {

            splitPane(
                viewBackend,
                { box { name(first) .. maxSize } },
                { box { name(divider) .. maxSize } },
                { box { name(second) .. maxSize } }
            ) .. width { paneWidth.dp }

        }.apply {

            assertLayoutParentFinal(first, 0, 0, paneWidth, firstHeight)
            assertLayoutParentFinal(divider, firstHeight - dividerShift, 0, paneWidth, dividerOverlaySize)
            assertLayoutParentFinal(second, firstHeight + dividerEffectiveSize, 0, paneWidth, testHeight - firstHeight - dividerEffectiveSize)

        }
    }

    @Test
    @JsName("paneContentSizeVerticalFirstProportional")
    fun `pane content size, vertical, first, proportional`() {
        val paneWidth = 50
        val split = 60.0

        val viewBackend = splitPaneBackend(SplitVisibility.First, SplitMethod.Proportional, split, Orientation.Vertical)

        snapshotTest {

            splitPane(
                viewBackend,
                { box { name(first) .. maxSize } },
                { box { name(divider) .. maxSize } },
                { box { name(second) .. maxSize } }
            ) .. width { paneWidth.dp }

        }.apply {

            assertLayoutParentFinal(first, 0, 0, paneWidth, testHeight)
            assertNotExist(divider)
            assertNotExist(second)

        }
    }

    @Test
    @JsName("wrapFromTop")
    fun wrapFromTop() {
        val wrapperSize = 20.0
        val wrappedHeight = 40

        val viewBackend = SplitPaneViewBackend(
            SplitVisibility.Both,
            SplitMethod.WrapSecond,
            wrapperSize,
            Orientation.Vertical,
            dividerOverlaySize = 0.dp,
            dividerEffectiveSize = 0.dp
        )

        snapshotTest {

            splitPane(
                viewBackend,
                { box { name(wrapper) .. maxSize } },
                { },
                { box { name(wrapped) .. maxWidth .. height { wrappedHeight.dp } } }
            ) .. maxWidth

        }.apply {

            assertLayoutParentFinal(wrapper, 0, 0, testWidth, wrapperSize)
            assertNotExist(divider)
            assertLayoutParentFinal(wrapped, wrapperSize, 0, testWidth, wrappedHeight)

        }
    }

    @Test
    @JsName("wrapFromBottom")
    fun wrapFromBottom() {
        val wrapperSize = 20.0
        val wrappedHeight = 40

        val viewBackend = SplitPaneViewBackend(
            SplitVisibility.Both,
            SplitMethod.WrapFirst,
            wrapperSize,
            Orientation.Vertical,
            dividerOverlaySize = 0.dp,
            dividerEffectiveSize = 0.dp
        )

        snapshotTest {

            splitPane(
                viewBackend,
                { box { name(wrapped) .. maxWidth .. height { wrappedHeight.dp } } },
                { },
                { box { name(wrapper) .. maxSize } },
            ) .. maxWidth

        }.apply {

            assertLayoutParentFinal(wrapped, 0, 0, testWidth, wrappedHeight)
            assertNotExist(divider)
            assertLayoutParentFinal(wrapper, 40, 0, testWidth, wrapperSize)

        }
    }

    @Test
    @JsName("wrapFromBottomUnbound")
    fun wrapFromBottomUnbound() {
        val wrapperSize = 20.0
        val wrappedSize = 40.0

        val viewBackend = SplitPaneViewBackend(
            SplitVisibility.Both,
            SplitMethod.WrapFirst,
            wrapperSize,
            Orientation.Vertical,
            dividerOverlaySize = 0.dp,
            dividerEffectiveSize = 0.dp
        )

        snapshotTest {

            flowBox {
                splitPane(
                    viewBackend,
                    { box { name(wrapped) .. size(wrappedSize.dp) } },
                    { },
                    { box { name(wrapper) .. size(wrapperSize.dp) } },
                ) .. maxWidth
            }
        }.apply {

            printLayout()

            assertLayoutParentFinal(wrapped, 0, 0, wrappedSize, wrappedSize)
            assertNotExist(divider)
            assertLayoutParentFinal(wrapper, wrappedSize, 0, wrapperSize, wrapperSize)

        }
    }
}