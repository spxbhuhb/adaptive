/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.layout

import hu.simplexion.adaptive.ui.common.fragment.grid
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.testing.adapter.uiTest
import hu.simplexion.adaptive.ui.common.testing.assertEquals
import kotlin.test.Test

class GridTest {

    @Test
    fun basicGridAsRoot() {

        uiTest(0, 0, 400, 100) {

            grid(colTemplate(100.dp, 0.25.fr, 0.75.fr), rowTemplate(1.fr), Grid1) {
                text("a", Grid1)
            }

        }.also { adapter ->

            adapter.assertEquals<Grid1>(0, 0, 400, 100)
            adapter.assertEquals<Text1>(0, 0, 100, 100)

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
                row(Row1) {
                    text("a", Size(92, 92), Text1)
                }
            }

        }.also { adapter ->

            adapter.assertEquals<Grid1>(0, 0, 375, 812)
            adapter.assertEquals<Grid1>(0, 0, 375, 812)
            adapter.assertEquals<Text1>(0, 0, 92, 92)

        }
    }

}