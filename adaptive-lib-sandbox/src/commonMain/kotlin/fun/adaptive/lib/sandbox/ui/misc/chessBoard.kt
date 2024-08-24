/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.sandbox.ui.misc

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.fragment.measureFragmentTime
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.instruction.layout.AlignItems
import `fun`.adaptive.ui.instruction.layout.Size

val black = color(0x000000u)
val white = color(0xffffffu)

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
                            row(*colors(r, c), AlignItems.Companion.center) {
                                text(r * chessBoardSize + c, Size(40.dp, 40.dp)) .. name("Square: ${r * chessBoardSize + c}")
                            }
                        }
                    }
                }
            }
        }
    }
}
