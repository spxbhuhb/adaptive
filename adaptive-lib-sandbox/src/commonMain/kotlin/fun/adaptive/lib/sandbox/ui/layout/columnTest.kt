/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.lib.sandbox.ui.layout

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.instruction.layout.AlignItems
import `fun`.adaptive.ui.instruction.layout.Alignment

@Adaptive
fun colTest() {
    column(name("outer-column"), padding(10.dp)) {
        gap(10.dp)

        text("Column Tests")
        columnBasic()

        for (v in Alignment.entries) {
            column {
                gap(10.dp)
                for (h in Alignment.entries) {
                    colAlign(AlignItems(v, h))
                }
            }
        }
    }
}

@Adaptive
private fun columnBasic() {
    layoutExample("Basic") {
        column {
            gap(10.dp)
            text("A A", greenishBackground)
            text("B B", blueishBackground)
        }
    }
}

@Adaptive
private fun colAlign(alignItems: AlignItems) {
    layoutExample("v ${alignItems.vertical} h ${alignItems.horizontal}") {
        column(alignItems) {
            gap(10.dp) .. maxSize
            text("A A", greenishBackground)
            text("B B", blueishBackground)
        }
    }
}