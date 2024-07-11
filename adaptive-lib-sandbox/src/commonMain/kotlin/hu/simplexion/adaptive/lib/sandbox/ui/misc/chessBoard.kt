/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.sandbox.ui.misc

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.fragment.measureFragmentTime
import hu.simplexion.adaptive.foundation.instruction.instructionsOf
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.foundation.rangeTo
import hu.simplexion.adaptive.ui.common.fragment.box
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.*

val black = Color(0x000000u)
val white = Color(0xffffffu)

val whiteSquare = instructionsOf(textColor(black), backgroundColor(white))
val blackSquare = instructionsOf(textColor(white), backgroundColor(black))

fun colors(r: Int, c: Int) =
    if (r % 2 == 1) {
        if (c % 2 == 0) {
            blackSquare
        } else {
            whiteSquare
        }
    } else {
        if (c % 2 == 1) {
            blackSquare
        } else {
            whiteSquare
        }
    }

const val chessBoardSize = 8

@Adaptive
fun chessBoard() {
    measureFragmentTime {
        box {
            height { (chessBoardSize * 40 + 2).dp }
            width { (chessBoardSize * 40 + 2).dp }
            border(black)

            column {
                for (r in 0 until chessBoardSize) {
                    row {
                        for (c in 1 .. chessBoardSize) {
                            row(*colors(r, c), AlignItems.center) {
                                text(r * chessBoardSize + c, Size(40.dp, 40.dp)) .. name("Square: ${r * chessBoardSize + c}")
                            }
                        }
                    }
                }
            }
        }
    }
}
