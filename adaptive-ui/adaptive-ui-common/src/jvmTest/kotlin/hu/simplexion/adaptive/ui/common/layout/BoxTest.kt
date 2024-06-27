/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.layout

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.rangeTo
import hu.simplexion.adaptive.ui.common.fragment.box
import hu.simplexion.adaptive.ui.common.fragment.space
import hu.simplexion.adaptive.ui.common.instruction.AlignItems.Companion.alignItems
import hu.simplexion.adaptive.ui.common.instruction.AlignSelf.Companion.alignSelf
import hu.simplexion.adaptive.ui.common.instruction.dp
import hu.simplexion.adaptive.ui.common.instruction.height
import hu.simplexion.adaptive.ui.common.instruction.position
import hu.simplexion.adaptive.ui.common.instruction.width
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
                space(*f1) .. F1 .. width { 120.dp } .. height { 20.dp }
                space(*f2) .. F2 .. width { 160.dp } .. height { 20.dp }
            }

        }.checks()
    }
}