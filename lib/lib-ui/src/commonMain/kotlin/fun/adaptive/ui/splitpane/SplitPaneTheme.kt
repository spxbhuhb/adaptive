package `fun`.adaptive.ui.splitpane

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.borderBottom
import `fun`.adaptive.ui.api.borderLeft
import `fun`.adaptive.ui.api.cursor
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.paddingHorizontal
import `fun`.adaptive.ui.api.paddingVertical
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.colors

class SplitPaneTheme(
    val dividerSize: DPixel = 9.dp,
    val color : Color
) {
    val splitDividerHorizontalVisible = instructionsOf(
        maxWidth, height { 1.dp }, borderBottom(color)
    )

    val splitDividerHorizontalOverlay = instructionsOf(
        maxWidth,
        height { dividerSize },
        //zIndex { 300 },
        paddingVertical { (dividerSize - 1.dp) / 2.dp },
        cursor.rowResize
    )

    val splitDividerVerticalVisible = instructionsOf(
        maxHeight, width { 1.dp }, borderLeft(color)
    )

    val splitDividerVerticalOverlay = instructionsOf(
        maxHeight,
        width { dividerSize },
        //zIndex { 300 },
        paddingHorizontal { (dividerSize - 1.dp) / 2.dp },
        cursor.colResize
    )

    companion object {
        val default = SplitPaneTheme(color = colors.lightOutline)
        val outline = SplitPaneTheme(color = colors.outline)
    }
}