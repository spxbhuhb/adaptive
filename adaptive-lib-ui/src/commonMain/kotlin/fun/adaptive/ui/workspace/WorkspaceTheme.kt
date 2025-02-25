package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textSmall

class WorkspaceTheme(
    val width: DPixel = 40.dp,
    val titleHeight: DPixel = 36.dp,
    val dividerSize: DPixel = 9.dp,
    val toolBackground: AdaptiveInstruction = backgrounds.surfaceVariant
) {

    companion object {
        var workspaceTheme = WorkspaceTheme()
    }

    val splitDividerHorizontalVisible = instructionsOf(
        maxWidth, height { 1.dp }, borderBottom(colors.outline)
    )

    val splitDividerHorizontalOverlay = instructionsOf(
        maxWidth,
        height { dividerSize },
        zIndex { 300 },
        paddingVertical { (dividerSize - 1.dp) / 2.dp },
        cursor.rowResize
    )

    val splitDividerVerticalVisible = instructionsOf(
        maxHeight, width { 1.dp }, borderLeft(colors.outline)
    )

    val splitDividerVerticalOverlay = instructionsOf(
        maxHeight,
        width { dividerSize },
        zIndex { 300 },
        paddingHorizontal { (dividerSize - 1.dp) / 2.dp },
        cursor.colResize
    )

    val paneIconColumn = instructionsOf(
        maxHeight,
        width { width },
        spaceBetween,
        zIndex { 200 },
        toolBackground
    )

    val rightIconColumn = paneIconColumn + borderLeft(colors.outline)
    val leftIconColumn = paneIconColumn + borderRight(colors.outline)

    val paneIconContainer = instructionsOf(
        size(width),
        margin { 6.dp },
        cornerRadius { 4.dp },
        alignItems.center
    )

    val paneIconDivider = instructionsOf(
        width { width },
        height { 16.dp },
        borderTop(colors.outline),
        margin(top = 8.dp, left = 8.dp, right = 8.dp, bottom = 7.dp)
    )

    val paneIcon = instructionsOf(
        svgFill(colors.onSurface)
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

    val paneTitleContainer = instructionsOf(
        maxWidth,
        height { titleHeight },
        spaceBetween,
        alignItems.center,
        backgrounds.surfaceVariant,
        paddingLeft { 12.dp },
        zIndex { 100 } // to have icon tooltips over items
    )

    val toolPaneTitleText = instructionsOf(
        textColors.onSurface,
        fontSize { 13.sp },
        semiBoldFont,
        noSelect
    )

    val toolPaneContainer = instructionsOf(
        rowTemplate(titleHeight, 1.fr),
        maxSize,
        toolBackground
    )

    val toolPaneContent = instructionsOf(
        maxSize,
        scroll
    )

}