/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.layout

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.instruction.Name
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.foundation.query.firstWith
import hu.simplexion.adaptive.foundation.rangeTo
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.instruction.AlignItems.Companion.alignItems
import hu.simplexion.adaptive.ui.common.instruction.AlignSelf.Companion.alignSelf
import hu.simplexion.adaptive.ui.common.support.C1
import hu.simplexion.adaptive.ui.common.support.F1
import hu.simplexion.adaptive.ui.common.support.F2
import hu.simplexion.adaptive.ui.common.testing.CommonTestAdapter
import hu.simplexion.adaptive.ui.common.testing.uiTest
import kotlin.test.Test

class BoxTest {

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

    fun AdaptiveInstruction.asArray() = arrayOf(this)

    fun cff(
        vararg instructions: AdaptiveInstruction,
        f1: Array<AdaptiveInstruction> = emptyArray(),
        f2: Array<AdaptiveInstruction> = emptyArray(),
        checks: CommonTestAdapter.() -> Unit
    ) {
        uiTest(0, 0, 400, 400) {

            box(C1, *instructions) {
                fillSpace

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
                                row(AlignItems.center) {
                                    name("row-$r-$c")
                                    text(r * size + c) .. Size(40.dp, 40.dp) .. name("cell-$r-$c")
                                }
                            }
                        }
                    }
                }
            }
        }.apply {
            println((firstWith<Name>() as AbstractCommonFragment<*>).dumpLayout(""))

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