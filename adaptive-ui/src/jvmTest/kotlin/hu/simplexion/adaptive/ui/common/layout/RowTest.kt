/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.layout

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.rangeTo
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.fragment.space
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.instruction.AlignItems.Companion.alignItems
import hu.simplexion.adaptive.ui.common.support.C1
import hu.simplexion.adaptive.ui.common.support.F1
import hu.simplexion.adaptive.ui.common.support.F2
import hu.simplexion.adaptive.ui.common.testing.CommonTestAdapter
import hu.simplexion.adaptive.ui.common.testing.uiTest
import kotlin.test.Test

class RowTest {

    @Test
    fun `row basic`() {
        cff {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, 0, 120, 20)
            assertFinal(F2, 0, 120, 160, 20)
        }
    }

    @Test
    fun gap() {

        cff(gap { 5.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, 0, 120, 20)
            assertFinal(F2, 0, 125, 160, 20)
        }
    }

    @Test
    fun `alignItems topStart gap 5`() {

        cff(alignItems.topStart, gap { 5.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, 0, 120, 20)
            assertFinal(F2, 0, 125, 160, 20)
        }
    }

    @Test
    fun `alignItems topCenter gap 6`() {

        cff(alignItems.topCenter, gap { 6.dp }) {

            val offset = (400 - 120 - 160 - 6) / 2

            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, offset, 120, 20)
            assertFinal(F2, 0, offset + 120 + 6, 160, 20)
        }
    }

    @Test
    fun `alignItems topEnd gap 6`() {

        cff(alignItems.topEnd, gap { 6.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, 400 - 160 - 6 - 120, 120, 20)
            assertFinal(F2, 0, 400 - 160, 160, 20)
        }
    }

    @Test
    fun `alignItems startCenter gap 6`() {

        cff(alignItems.startCenter, gap { 6.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, (400 - 20) / 2, 0, 120, 20)
            assertFinal(F2, (400 - 20) / 2, 120 + 6, 160, 20)
        }
    }

    @Test
    fun `alignItems startBottom gap 6`() {

        cff(alignItems.startBottom, gap { 6.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 400 - 20, 0, 120, 20)
            assertFinal(F2, 400 - 20, 120 + 6, 160, 20)
        }
    }

    @Test
    fun `alignItems bottomCenter gap 6`() {

        cff(alignItems.bottomCenter, gap { 6.dp }) {

            val offset = (400 - 120 - 160 - 6) / 2

            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 400 - 20, offset, 120, 20)
            assertFinal(F2, 400 - 20, offset + 120 + 6, 160, 20)
        }
    }

    @Test
    fun `alignItems bottomEnd gap 6`() {

        cff(alignItems.bottomEnd, gap { 6.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 400 - 20, 400 - 160 - 6 - 120, 120, 20)
            assertFinal(F2, 400 - 20, 400 - 160, 160, 20)
        }
    }


    @Test
    fun `padding basic`() {
        cff(padding { 5.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 5, 5, 120, 20)
            assertFinal(F2, 5, 5 + 120, 160, 20)
        }
    }

    @Test
    fun `alignItems topStart padding 10`() {
        cff(alignItems.topStart, padding { 10.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 10, 10, 120, 20)
            assertFinal(F2, 10, 130, 160, 20)
        }
    }

    @Test
    fun `alignItems startCenter padding 15`() {
        cff(alignItems.startCenter, padding { 16.dp }) {
            val offset = (400 - 20) / 2
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, offset, 16, 120, 20)
            assertFinal(F2, offset, 136, 160, 20)
        }
    }

    @Test
    fun `alignItems bottomEnd padding 30`() {
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
    fun `spaceAround margin 33`() {
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
    fun `spaceBetween margin 20`() {
        cff(spaceBetween, margin { 20.dp }) {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 20, 20, 120, 20)
            assertFinal(F2, 20, 400 - 160 - 20, 160, 20)
        }
    }

    fun cff(vararg instructions: AdaptiveInstruction, checks: CommonTestAdapter.() -> Unit) {
        uiTest(0, 0, 400, 400) {

            row(C1, *instructions) {
                maxSize
                space() .. F1 .. width { 120.dp } .. height { 20.dp }
                space() .. F2 .. width { 160.dp } .. height { 20.dp }
            }

        }.checks()
    }
}