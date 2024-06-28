/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.sandbox.ui.layout

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.rangeTo
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.flowBox
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.dp
import hu.simplexion.adaptive.ui.common.instruction.gap
import hu.simplexion.adaptive.ui.common.instruction.padding

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