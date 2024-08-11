/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.layout

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.common.fragment.*
import `fun`.adaptive.ui.common.instruction.*
import `fun`.adaptive.ui.common.support.C1
import `fun`.adaptive.ui.common.support.F1
import `fun`.adaptive.ui.common.support.F2
import `fun`.adaptive.ui.common.testing.CommonTestAdapter
import `fun`.adaptive.ui.common.testing.uiTest
import kotlin.test.Test

class GridTest {

    @Test
    fun `basic fixed fixed`() {
        cff(
            colTemplate(120.dp, 160.dp),
            rowTemplate(20.dp)
        ) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, 0, 120, 20)
            assertFinal(F2, 0, 120, 160, 20)
        }
    }

    @Test
    fun `basic fixed fraction`() {
        cff(
            colTemplate(140.dp, 1.fr),
            rowTemplate(20.dp)
        ) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, 0, 140, 20)
            assertFinal(F2, 0, 140, 400 - 140, 20)
        }
    }

    @Test
    fun `gap horizontal 5`() {
        cff(
            colTemplate(120.dp, 160.dp),
            rowTemplate(20.dp),
            gapWidth { 5.dp }
        ) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, 0, 120, 20)
            assertFinal(F2, 0, 125, 160, 20)
        }
    }

    @Test
    fun `gap vertical 5`() {
        cff(
            colTemplate(120.dp),
            rowTemplate(20.dp, 30.dp),
            gapHeight { 5.dp }
        ) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, 0, 120, 20)
            assertFinal(F2, 25, 0, 120, 30)
        }
    }

    fun cff(
        vararg instructions: AdaptiveInstruction,
        f1: Array<AdaptiveInstruction> = emptyArray(),
        f2: Array<AdaptiveInstruction> = emptyArray(),
        checks: CommonTestAdapter.() -> Unit
    ) {
        uiTest(0, 0, 400, 400) {

            grid(C1, *instructions) {
                space(*f1) .. F1
                space(*f2) .. F2
            }

        }.checks()
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

                    slot {
                        text("Click") .. maxWidth .. name("slot-text")
                    }
                }
            }
        }.apply {

            assertFinal(name("grid-1"), 0, 0, 500, 400)

            assertFinal(name("column-1"), 0, 0, 200, 400)
            assertFinal(name("hello"), 10, 10, 180, 20)
            assertFinal(name("world"), 34, 10, 180, 20)

            assertFinal(name("column-2"), 0, 200, 300, 400)
            assertFinal(name("slot-text"), 10, 10, 280, 20)

        }
    }
}