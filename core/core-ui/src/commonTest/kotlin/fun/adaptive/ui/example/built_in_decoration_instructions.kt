package `fun`.adaptive.ui.example

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun borderExamples() {
    box {
        border { color(0x0) } // black border, 1.dp, all directions (top, right, bottom, left)
    }
    box {
        border(color(0x0)) // black border, 1.dp, all directions (top, right, bottom, left)
    }
    box {
        border(color(0xff0000u), 2.dp) // red border, 2.dp, all directions (top, right, bottom, left)
    }
    box {
        border(color(0x00ff00), 1.dp, 2.dp, 3.dp, 4.dp) // green border, 1.dp top, 2.dp right, 3.dp bottom, 4.dp left
    }
    box {
        border(color(0x0000ff), bottom = 1.dp, left = 2.dp) // blue border, 0.dp top, 0.dp right,13.dp bottom, 2.dp left
    }
    box {
        borderTop(color(0x0000ff), 1.dp) // blue border, 1.dp, top only
    }
    box {
        borderRight(color(0x0), 1.dp) // black border, 1.dp, right only
    }
    box {
        borderBottom(color(0xff0000), 1.dp) // red border, 1.dp, bottom only
    }
    box {
        borderLeft(color(0x00ff00), 1.dp) // green border, 1.dp, left only
    }
}

@Adaptive
fun backgroundColorExample() {

}