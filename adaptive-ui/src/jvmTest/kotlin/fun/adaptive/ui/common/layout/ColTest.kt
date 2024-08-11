/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.layout

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.common.fragment.column
import `fun`.adaptive.ui.common.fragment.space
import `fun`.adaptive.ui.common.instruction.*
import `fun`.adaptive.ui.common.support.C1
import `fun`.adaptive.ui.common.support.F1
import `fun`.adaptive.ui.common.support.F2
import `fun`.adaptive.ui.common.testing.CommonTestAdapter
import `fun`.adaptive.ui.common.testing.uiTest
import kotlin.test.Test

class ColTest {

    @Test
    fun `col basic`() {
        cff {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, 0, 120, 20)
            assertFinal(F2, 20, 0, 160, 20)
        }
    }

    fun cff(vararg instructions: AdaptiveInstruction, checks: CommonTestAdapter.() -> Unit) {
        uiTest(0, 0, 400, 400) {

            column(C1, *instructions) {
                maxSize

                space() .. F1 .. width { 120.dp } .. height { 20.dp }
                space() .. F2 .. width { 160.dp } .. height { 20.dp }
            }

        }.checks()
    }

    @Test
    fun `for and if`() {
        uiTest(0, 0, 400, 400) {
            column {
                C1 .. maxSize

                gap(10.dp)
                for (t in listOf("A A", "B B")) {
                    when (t) {
                        "A A" -> space() .. F1 .. width { 120.dp } .. height { 20.dp }
                        "B B" -> space() .. F2 .. width { 160.dp } .. height { 20.dp }
                    }
                }
            }
        }.apply {
            assertFinal(C1, 0, 0, 400, 400)
            assertFinal(F1, 0, 0, 120, 20)
            assertFinal(F2, 30, 0, 160, 20)
        }
    }
}