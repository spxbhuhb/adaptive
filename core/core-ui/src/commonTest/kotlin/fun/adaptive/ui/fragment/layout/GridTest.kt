package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.gapHeight
import `fun`.adaptive.ui.api.gapWidth
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.space
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.support.C1
import `fun`.adaptive.ui.support.F1
import `fun`.adaptive.ui.support.F2
import `fun`.adaptive.ui.support.F3
import `fun`.adaptive.ui.testing.AuiTestAdapter
import `fun`.adaptive.ui.testing.uiTest
import kotlin.test.Test

class GridTest {

    @Test
    fun basic_fixed_fixed() {
        cff(
            colTemplate(120.dp, 160.dp),
            rowTemplate(20.dp)
        ) {
            assertFinal(C1, 0, 0, 280, 20)
            assertFinal(F1, 0, 0, 120, 20)
            assertFinal(F2, 0, 120, 160, 20)
        }
    }

    @Test
    fun basic_fixed_fraction() {
        cff(
            maxWidth,
            colTemplate(140.dp, 1.fr),
            rowTemplate(20.dp)
        ) {
            assertFinal(C1, 0, 0, 400, 20)
            assertFinal(F1, 0, 0, 140, 20)
            assertFinal(F2, 0, 140, 400 - 140, 20)
        }
    }

    @Test
    fun gap_horizontal_5() {
        cff(
            colTemplate(120.dp, 160.dp),
            rowTemplate(20.dp),
            gapWidth { 5.dp }
        ) {
            assertFinal(C1, 0, 0, 285, 20)
            assertFinal(F1, 0, 0, 120, 20)
            assertFinal(F2, 0, 125, 160, 20)
        }
    }

    @Test
    fun gap_vertical_5() {
        cff(
            colTemplate(120.dp),
            rowTemplate(20.dp, 30.dp),
            gapHeight { 5.dp }
        ) {
            assertFinal(C1, 0, 0, 120, 55)
            assertFinal(F1, 0, 0, 120, 20)
            assertFinal(F2, 25, 0, 120, 30)
        }
    }

    fun cff(
        vararg testInstructions: AdaptiveInstruction,
        checks: AuiTestAdapter.() -> Unit
    ) {
        uiTest(0, 0, 400, 400) {

            grid(C1, AdaptiveInstructionGroup(testInstructions)) {
                space() .. F1
                space() .. F2
            }

        }.checks()
    }

    @Test
    fun lastGap() {
        uiTest(0, 0, 400, 400) {
            grid {
                C1
                alignItems.startCenter
                colTemplate(120.dp)
                rowTemplate(20.dp repeat 3)
                gap { 10.dp }

                space() .. F1 .. height { 20.dp }
                space() .. F2 .. height { 20.dp }
                space() .. F3 .. height { 20.dp }
            }
        }.apply {
            assertFinal(C1, 0, 0, 120, 80)
            assertFinal(F1, 0, 0, 120, 20)
            assertFinal(F2, 30, 0, 120, 20)
            assertFinal(F3, 60, 0, 120, 20)
        }
    }

    @Test
    fun sidebar() {
        uiTest(0, 0, 500, 400) {
            grid {
                maxSize
                colTemplate(200.dp, 1.fr) .. name("grid-1")

                column {
                    maxSize .. padding(10.dp) .. gap(4.dp) .. name("column-1")

                    text("Hello") .. maxWidth .. name("hello")
                    text("World") .. maxWidth .. name("world")
                }

                column {
                    maxSize .. verticalScroll .. padding { 10.dp } .. name("column-2")
                    text("Click") .. maxWidth .. name("column-2-text")
                }
            }
        }.apply {

            assertFinal(name("grid-1"), 0, 0, 500, 400)

            assertFinal(name("column-1"), 0, 0, 200, 400)
            assertFinal(name("hello"), 10, 10, 180, 20)
            assertFinal(name("world"), 34, 10, 180, 20)

            assertFinal(name("column-2"), 0, 200, 300, 400)
            assertFinal(name("column-2-text"), 10, 10, 280, 20)

        }
    }
}