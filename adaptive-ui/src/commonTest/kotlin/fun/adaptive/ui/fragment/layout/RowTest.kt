package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.margin
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.space
import `fun`.adaptive.ui.api.spaceAround
import `fun`.adaptive.ui.api.spaceBetween
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.support.C1
import `fun`.adaptive.ui.support.F1
import `fun`.adaptive.ui.support.F2
import `fun`.adaptive.ui.testing.AuiTestAdapter
import `fun`.adaptive.ui.testing.uiTest
import kotlin.test.Test

class RowTest {

    @Test
    fun row_basic() {
        cff {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, 0, 120, 20)
            assertFinal(F2, 0, 120, 160, 20)
        }
    }

    @Test
    fun gap_5() {
        cff(gap { 5.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, 0, 120, 20)
            assertFinal(F2, 0, 125, 160, 20)
        }
    }

    @Test
    fun alignItems_topStart_gap_5() {

        cff(alignItems.topStart, gap { 5.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, 0, 120, 20)
            assertFinal(F2, 0, 125, 160, 20)
        }
    }

    @Test
    fun alignItems_topCenter_gap_6() {

        cff(alignItems.topCenter, gap { 6.dp }) {

            val offset = (400 - 120 - 160 - 6) / 2

            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, offset, 120, 20)
            assertFinal(F2, 0, offset + 120 + 6, 160, 20)
        }
    }

    @Test
    fun alignItems_topEnd_gap_6() {

        cff(alignItems.topEnd, gap { 6.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, 400 - 160 - 6 - 120, 120, 20)
            assertFinal(F2, 0, 400 - 160, 160, 20)
        }
    }

    @Test
    fun alignItems_startCenter_gap_6() {

        cff(alignItems.startCenter, gap { 6.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, (400 - 20) / 2, 0, 120, 20)
            assertFinal(F2, (400 - 20) / 2, 120 + 6, 160, 20)
        }
    }

    @Test
    fun alignItems_startBottom_gap_6() {

        cff(alignItems.startBottom, gap { 6.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 400 - 20, 0, 120, 20)
            assertFinal(F2, 400 - 20, 120 + 6, 160, 20)
        }
    }

    @Test
    fun alignItems_bottomCenter_gap_6() {

        cff(alignItems.bottomCenter, gap { 6.dp }) {

            val offset = (400 - 120 - 160 - 6) / 2

            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 400 - 20, offset, 120, 20)
            assertFinal(F2, 400 - 20, offset + 120 + 6, 160, 20)
        }
    }

    @Test
    fun alignItems_bottomEnd_gap_6() {

        cff(alignItems.bottomEnd, gap { 6.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 400 - 20, 400 - 160 - 6 - 120, 120, 20)
            assertFinal(F2, 400 - 20, 400 - 160, 160, 20)
        }
    }


    @Test
    fun padding_basic() {
        cff(padding { 5.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 5, 5, 120, 20)
            assertFinal(F2, 5, 5 + 120, 160, 20)
        }
    }

    @Test
    fun alignItems_topStart_padding_10() {
        cff(alignItems.topStart, padding { 10.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 10, 10, 120, 20)
            assertFinal(F2, 10, 130, 160, 20)
        }
    }

    @Test
    fun alignItems_startCenter_padding_15() {
        cff(alignItems.startCenter, padding { 16.dp }) {
            val offset = (400 - 20) / 2
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, offset, 16, 120, 20)
            assertFinal(F2, offset, 136, 160, 20)
        }
    }

    @Test
    fun alignItems_bottomEnd_padding_30() {
        cff(alignItems.bottomEnd, padding { 30.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, (400 - 30 - 20), (400 - 30 - 160 - 120), 120, 20)
            assertFinal(F2, (400 - 30 - 20), (400 - 30 - 160), 160, 20)
        }
    }

    @Test
    fun spaceAround() {
        cff(spaceAround) {
            val gap = (400 - 120 - 160) / 3
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, gap, 120, 20)
            assertFinal(F2, 0, gap + 120 + gap, 160, 20)
        }
    }

    @Test
    fun spaceAround_margin_33() {
        cff(spaceAround, margin { 33.dp }) {
            val gap = (400 - 120 - 160 - 33 - 33) / 3
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 33, 33 + gap, 120, 20)
            assertFinal(F2, 33, 33 + gap + 120 + gap, 160, 20)
        }
    }

    @Test
    fun spaceBetween() {
        cff(spaceBetween) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, 0, 120, 20)
            assertFinal(F2, 0, 400 - 160, 160, 20)
        }
    }

    @Test
    fun spaceBetween_margin_20() {
        cff(spaceBetween, margin { 20.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 20, 20, 120, 20)
            assertFinal(F2, 20, 400 - 160 - 20, 160, 20)
        }
    }

    fun cff(vararg instructions: AdaptiveInstruction, checks: AuiTestAdapter.() -> Unit) {
        uiTest(0, 0, 400, 400) {

            row(C1, *instructions) {
                maxSize
                space() .. F1 .. width { 120.dp } .. height { 20.dp }
                space() .. F2 .. width { 160.dp } .. height { 20.dp }
            }

        }.checks()
    }
}