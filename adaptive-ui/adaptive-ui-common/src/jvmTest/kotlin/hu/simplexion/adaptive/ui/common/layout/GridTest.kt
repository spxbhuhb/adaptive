/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.layout

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.rangeTo
import hu.simplexion.adaptive.ui.common.fragment.grid
import hu.simplexion.adaptive.ui.common.fragment.space
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.support.C1
import hu.simplexion.adaptive.ui.common.support.F1
import hu.simplexion.adaptive.ui.common.support.F2
import hu.simplexion.adaptive.ui.common.testing.CommonTestAdapter
import hu.simplexion.adaptive.ui.common.testing.uiTest
import kotlin.test.Test

class GridTest {

    @Test
    fun basic() {
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

    fun AdaptiveInstruction.asArray() = arrayOf(this)

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
}