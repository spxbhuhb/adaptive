/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.layout

import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.testing.uiTest
import hu.simplexion.adaptive.ui.common.testing.assertEquals
import kotlin.test.Test

class RowTest {

    @Test
    fun basicRowAsRoot() {

        uiTest(0, 0, 400, 400) {

            row(Row1) {
                text("almafa", Text1) // 6 char = 120f
                text("barakcfa", Text2) // 8 char = 160f
            }

        }.also { adapter ->

            adapter.assertEquals<Row1>(0, 0, 400, 400)
            adapter.assertEquals<Text1>(0, 0, 120, 20)
            adapter.assertEquals<Text2>(0, 120, 160, 20)

        }

    }

}