package `fun`.adaptive.ui.splitpane

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun splitPaneDivider() {
    box {
        maxHeight
        width { 9.dp }
        zIndex { 300 }
        paddingHorizontal { (9.dp - 1.dp) / 2.dp }
        cursor.colResize

        box {
            maxHeight
            width { 1.dp }
            borderLeft(colors.outline)
        }
    }
}