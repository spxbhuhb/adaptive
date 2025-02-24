package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.borderLeft
import `fun`.adaptive.ui.api.borderRight
import `fun`.adaptive.ui.api.borderTop
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.margin
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.paddingHorizontal
import `fun`.adaptive.ui.api.paddingVertical
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.spaceBetween
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.api.zIndex
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

    val panelIconContainer = instructionsOf(
        size(width),
        margin { 6.dp },
        cornerRadius { 4.dp },
        alignItems.center
    )

    val panelIcon = instructionsOf(

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