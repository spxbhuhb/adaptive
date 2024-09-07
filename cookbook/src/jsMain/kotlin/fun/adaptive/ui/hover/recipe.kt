package `fun`.adaptive.ui.hover

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.hover
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun hoverMain() {
    val hover = hover()

    column {
        box {
            width { 32.dp } .. height { 32.dp }
            if (hover) backgroundColor(0xff0000) else backgroundColor(0x00ff00)
        }

        if (hover) text("hover")
    }

}