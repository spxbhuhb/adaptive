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

class GridTest {

    object gridTag : AdaptiveInstruction
    object textTag1 : AdaptiveInstruction

    @Test
    fun basicGridAsRoot() {

        val adapter = uiTest(0f, 0f, 400f, 100f) {
            grid(ColTemplate(100.dp, 0.25.fr, 0.75.fr), RowTemplate(1.fr), gridTag) {
                text("a", textTag1)
            }
        }

        adapter.assertEquals<gridTag>(0f, 0f, 400f, 100f)
        adapter.assertEquals<textTag1>(0f, 0f, 100f, 100f)
    }
}