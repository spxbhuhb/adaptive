/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.sandbox.ui.layout

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.common.fragment.column
import `fun`.adaptive.ui.common.fragment.flowBox
import `fun`.adaptive.ui.common.fragment.text
import `fun`.adaptive.ui.common.instruction.dp
import `fun`.adaptive.ui.common.instruction.gap
import `fun`.adaptive.ui.common.instruction.padding

@Adaptive
fun flowBoxTest() {
    column {
        padding(10.dp) .. gap(10.dp)

        text("FlowBox Tests")

        basic()


    }
}

@Adaptive
private fun basic() {
    layoutExample("Basic") {
        flowBox {
            gap(10.dp)
            text("A A", greenishBackground)
            text("B B", blueishBackground)
            text("A A", greenishBackground)
            text("B B", blueishBackground)
            text("A A", greenishBackground)
            text("B B", blueishBackground)
            text("A A", greenishBackground)
            text("B B", blueishBackground)
            text("A A", greenishBackground)
            text("B B", blueishBackground)
        }
    }
}