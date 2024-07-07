/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support

import hu.simplexion.adaptive.ui.common.fragment.grid
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.testing.uiTest
import kotlin.test.Test

class GridTest {

    @Test
    fun basicGridAsRoot() {

        uiTest(0, 0, 400, 100) {

            grid(colTemplate(100.dp, 0.25.fr, 0.75.fr), rowTemplate(1.fr), Grid1) {
                text("a", F1)
            }

        }.also { adapter ->

            adapter.assertFinal(Grid1, 0, 0, 400, 100)
            adapter.assertFinal(F1, 0, 0, 20, 20)

        }
    }

    @Test
    fun gridWithRow() {

        uiTest(0, 0, 375, 812) {

            grid(
                rowTemplate(260.dp, 1.fr, 100.dp, 100.dp),
                colTemplate(1.fr),
                Grid1
            ) {
                row(C1) {
                    text("a", Size(92.dp, 92.dp), F1)
                }
            }

        }.also { adapter ->

            adapter.assertFinal(Grid1, 0, 0, 375, 812)
            adapter.assertFinal(Grid1, 0, 0, 375, 812)
            adapter.assertFinal(F1, 0, 0, 92, 92)

        }
    }
}