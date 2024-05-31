/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.layout

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.fragment.grid
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.testing.adapter.uiTest
import hu.simplexion.adaptive.ui.common.testing.assertEquals
import kotlin.test.Test

class RowTest {

    object textTag1 : AdaptiveInstruction
    object textTag2 : AdaptiveInstruction
    object rowTag : AdaptiveInstruction

    @Test
    fun basicRowAsRoot() {

        val adapter = uiTest(0f, 0f, 400f, 400f) {
            row(rowTag) {
                text("almafa", textTag1) // 6 char = 120f
                text("barakcfa", textTag2) // 8 char = 160f
            }
        }

        adapter.assertEquals<rowTag>(0f, 0f, 400f, 400f)
        adapter.assertEquals<textTag1>(0f, 0f, 120f, 20f)
        adapter.assertEquals<textTag2>(0f, 120f, 160f, 20f)
    }

}