package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textSmall

class WorkspaceTheme(
    val width: DPixel = 40.dp
) {

    companion object {
        var workspaceTheme = WorkspaceTheme()
    }

    val iconColumn = instructionsOf(
        maxHeight,
        width { width },
        spaceBetween
    )

    val rightIconColumn = iconColumn + borderLeft(colors.outline)
    val leftIconColumn = iconColumn + borderRight(colors.outline)

    val divider = instructionsOf(
        width { width },
        height { 16.dp },
        borderTop(colors.outline),
        margin(top = 8.dp, left = 8.dp, right = 8.dp, bottom = 7.dp)
    )

    val paneIconContainer = instructionsOf(
        size(width),
        margin { 6.dp },
        cornerRadius { 4.dp },
        alignItems.center
    )

    val paneIcon = instructionsOf(

    )

    val tooltipContainer = instructionsOf(
        paddingHorizontal { 16.dp },
        paddingVertical { 8.dp },
        backgrounds.reverse,
        cornerRadius { 4.dp },
        gap { 8.dp },
        zIndex { 100 }
    )

    val tooltipTextBase = instructionsOf(
        noSelect,
        textSmall
    )

    val tooltipName = tooltipTextBase + textColors.onReverse

    val tooltipShortcut = tooltipTextBase + textColors.onReverseVariant

}