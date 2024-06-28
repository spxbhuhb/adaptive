/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.sandbox.layout

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.foundation.rangeTo
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.*

@Adaptive
fun rowTest() {
    column(name("outer-column"), padding(10.dp)) {
        gap(10.dp)

        text("Row Tests")
        rowBasic()

        for (v in Alignment.entries) {
            column {
                gap(10.dp)
                for (h in Alignment.entries) {
                    rowAlign(AlignItems(v, h))
                }
            }
        }
    }
}

@Adaptive
private fun rowBasic() {
    layoutExample("Basic") {
        row {
            gap(10.dp)
            text("A A", greenishBackground)
            text("B B", blueishBackground)
        }
    }
}

@Adaptive
private fun rowAlign(alignItems: AlignItems) {
    layoutExample("v ${alignItems.vertical} h ${alignItems.horizontal}") {
        row(alignItems) {
            gap(10.dp) .. maxSize
            text("A A", greenishBackground)
            text("B B", blueishBackground)
        }
    }
}