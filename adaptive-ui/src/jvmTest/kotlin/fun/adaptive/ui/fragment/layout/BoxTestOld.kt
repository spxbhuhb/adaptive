/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.Name
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.query.firstWith
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.space
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.api.dumpLayout
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.instruction.layout.AlignItems
import `fun`.adaptive.ui.instruction.layout.Size
import `fun`.adaptive.ui.support.C1
import `fun`.adaptive.ui.support.F1
import `fun`.adaptive.ui.support.F2
import `fun`.adaptive.ui.testing.AuiTestAdapter
import `fun`.adaptive.ui.testing.uiTest
import kotlin.test.Test

class BoxTestOld {

    @Test
    fun `box basic`() {
        cff {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, 0, 120, 20)
            assertFinal(F2, 0, 0, 160, 20)
        }
    }

    @Test
    fun `box positions`() {
        cff(
            f1 = position(100.dp, 100.dp).asArray(),
            f2 = position(90.dp, 80.dp).asArray(),
        ) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 100, 100, 120, 20)
            assertFinal(F2, 90, 80, 160, 20)
        }
    }

    @Test
    fun `box alignItem bottomCenter position`() {
        // as position is instructed, alignment should be ignored
        cff(
            alignItems.bottomCenter,
            f1 = position(100.dp, 100.dp).asArray(),
            f2 = position(90.dp, 80.dp).asArray(),
        ) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 100, 100, 120, 20)
            assertFinal(F2, 90, 80, 160, 20)
        }
    }

    @Test
    fun `box alignItem bottomCenter`() {
        cff(
            alignItems.bottomCenter,
            f2 = position(90.dp, 80.dp).asArray(),
        ) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 400 - 20, (400 - 120) / 2, 120, 20)
            assertFinal(F2, 90, 80, 160, 20)
        }
    }

    @Test
    fun `box alignSelf bottomCenter`() {
        cff(
            f1 = alignSelf.bottomCenter.asArray(),
            f2 = position(90.dp, 80.dp).asArray(),
        ) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 400 - 20, (400 - 120) / 2, 120, 20)
            assertFinal(F2, 90, 80, 160, 20)
        }
    }

    @Test
    fun `box border`() {
        cff(
            border(color(0u)),
            f2 = alignSelf.bottomEnd.asArray(),
        ) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 1, 1, 120, 20)
            assertFinal(F2, 400 - 20 - 1, 400 - 160 - 1, 160, 20)
        }
    }

    fun AdaptiveInstruction.asArray() = arrayOf(this)

    fun cff(
        vararg testInstructions: AdaptiveInstruction,
        f1: Array<AdaptiveInstruction> = emptyArray(),
        f2: Array<AdaptiveInstruction> = emptyArray(),
        checks: AuiTestAdapter.() -> Unit
    ) {
        uiTest(0, 0, 400, 400) {

            box(C1, AdaptiveInstructionGroup(testInstructions)) {
                maxSize

                space(*f1) .. F1 .. width { 120.dp } .. height { 20.dp }
                space(*f2) .. F2 .. width { 160.dp } .. height { 20.dp }
            }

        }.checks()
    }

    @Test
    fun chessBoard() {
        uiTest(0, 0, 400, 400) {
            val size = 2
            box {
                name("box")
                height { (size * 40).dp }
                width { (size * 40).dp }

                column {
                    name("col")

                    for (r in 0 until size) {
                        row {
                            name("row-$r")

                            for (c in 1 .. size) {
                                row(AlignItems.Companion.center) {
                                    name("row-$r-$c")
                                    text(r * size + c) .. Size(40.dp, 40.dp) .. name("cell-$r-$c")
                                }
                            }
                        }
                    }
                }
            }
        }.apply {
            println((firstWith<Name>() as AbstractAuiFragment<*>).dumpLayout(""))

            assertFinal(name("box"), 0, 0, 80, 80)
            assertFinal(name("col"), 0, 0, 80, 80)
            assertFinal(name("row-0"), 0, 0, 80, 40)
            assertFinal(name("row-0-1"), 0, 0, 40, 40)
            assertFinal(name("cell-0-1"), 0, 0, 40, 40)
            assertFinal(name("row-0-2"), 0, 40, 40, 40)
            assertFinal(name("cell-0-2"), 0, 0, 40, 40)
        }
    }

}