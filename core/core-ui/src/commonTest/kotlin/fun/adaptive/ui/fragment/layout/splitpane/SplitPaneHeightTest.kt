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
    fun `pane content height should respect pane height`() {
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

            snapshot().encodeToPrettyJson().debug()

            assertLayoutParentFinal(first, 0, 0, firstWidth, paneHeight)
            assertLayoutParentFinal(divider, 0, firstWidth - dividerShift, dividerOverlaySize, paneHeight)
            assertLayoutParentFinal(second, 0, firstWidth + dividerEffectiveSize, testWidth - firstWidth - dividerEffectiveSize, paneHeight)

        }
    }

}