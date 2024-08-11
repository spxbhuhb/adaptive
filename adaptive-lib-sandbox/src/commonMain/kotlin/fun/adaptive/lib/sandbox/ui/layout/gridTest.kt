/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.sandbox.ui.layout/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.common.fragment.box
import `fun`.adaptive.ui.common.fragment.column
import `fun`.adaptive.ui.common.fragment.grid
import `fun`.adaptive.ui.common.fragment.text
import `fun`.adaptive.ui.common.instruction.*

@Adaptive
fun gridTest() {
    column(name("outer-column"), padding(10.dp)) {
        gap(10.dp)

        text("Grid Tests")
        grid1fr()
        gridMargin()
        gridBorder()
        gridPadding()
        gridMarginBorderPadding()
    }
}

@Adaptive
private fun grid1fr() {
    layoutExample("1fr greenish") {
        grid {
            greenishBackground
        }
    }
}


@Adaptive
private fun gridMargin() {
    layoutExample("1fr greenish m 10") {
        grid {
            margin(10.dp) .. greenishBackground

            box {
                size(20.dp, 20.dp) .. blueishBackground
            }
        }
    }
}

@Adaptive
private fun gridBorder() {
    layoutExample("1fr greenish b 4") {
        grid(trace) {
            border(innerBorder, 4.dp) .. greenishBackground

            box(trace) {
                blueishBackground
            }
        }
    }
}

@Adaptive
private fun gridPadding() {
    layoutExample("1fr greenish p 10") {
        grid {
            padding(10.dp) .. greenishBackground

            box {
                blueishBackground
            }
        }
    }
}

@Adaptive
private fun gridMarginBorderPadding() {
    layoutExample("1fr greenish m 10 b 4 p 10") {
        grid {
            greenishBackground
            margin(10.dp)
            border(innerBorder, 4.dp)
            padding(10.dp)

            box {
                blueishBackground
            }
        }
    }
}