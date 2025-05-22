package `fun`.adaptive.ui.fragment.layout.splitpane

import `fun`.adaptive.adat.encodeToPrettyJson
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.fragment.layout.SplitPaneViewBackend.Companion.splitPaneBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.testing.snapshotTest
import `fun`.adaptive.utility.debug
import kotlin.test.Test

class SplitPaneHeightTest {

    val first = "first"
    val divider = "divider"
    val second = "second"

    @Test
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

}