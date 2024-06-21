/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package misc/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.fragment.measureFragmentTime
import hu.simplexion.adaptive.ui.common.fragment.box
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.*

val black = color(0x000000u)
val white = color(0xffffffu)

fun colors(r: Int, c: Int) =
    if (r % 2 == 1) {
        if (c % 2 == 0) {
            arrayOf(white, BackgroundColor(black))
        } else {
            arrayOf(black, BackgroundColor(white))
        }
    } else {
        if (c % 2 == 1) {
            arrayOf(white, BackgroundColor(black))
        } else {
            arrayOf(black, BackgroundColor(white))
        }
    }

const val size = 8

@Adaptive
fun chessBoard() {
    measureFragmentTime {
        box {
            height { (size * 40 + 2).dp }
            width { (size * 40 + 2).dp }
            border(black)

            column {
                for (r in 0 until size) {
                    row {
                        for (c in 1 .. size) {
                            row(*colors(r, c), AlignItems.center) { text(r * size + c, Size(40.dp, 40.dp)) }
                        }
                    }
                }
            }
        }
    }
}
