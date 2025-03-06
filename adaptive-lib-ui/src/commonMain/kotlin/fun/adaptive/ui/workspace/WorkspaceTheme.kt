package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.*
import `fun`.adaptive.utility.UUID

class WorkspaceTheme(
    val width: DPixel = 40.dp,
    val titleHeight: DPixel = 36.dp,
    val dividerSize: DPixel = 9.dp,
    toolBackground: AdaptiveInstruction = backgrounds.surfaceVariant,
    val toolBorderColor: Color = colors.lightOutline
) {

    companion object {
        var workspaceTheme = WorkspaceTheme()
    }

    val splitDividerHorizontalVisible = instructionsOf(
        maxWidth, height { 1.dp }, borderBottom(toolBorderColor)
    )

    val splitDividerHorizontalOverlay = instructionsOf(
        maxWidth,
        height { dividerSize },
        //zIndex { 300 },
        paddingVertical { (dividerSize - 1.dp) / 2.dp },
        cursor.rowResize
    )

    val splitDividerVerticalVisible = instructionsOf(
        maxHeight, width { 1.dp }, borderLeft(toolBorderColor)
    )

    val splitDividerVerticalOverlay = instructionsOf(
        maxHeight,
        width { dividerSize },
        //zIndex { 300 },
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

    val rightIconColumn = paneIconColumn + borderLeft(toolBorderColor)
    val leftIconColumn = paneIconColumn + borderRight(toolBorderColor)

    fun paneIconContainer(
        thisPane: WorkspacePane,
        activePane: UUID<WorkspacePane>?,
        focusedPane: UUID<WorkspacePane>?,
        hover: Boolean
    ) =
        paneIconContainer(
            thisPane.uuid == activePane,
            thisPane.uuid == focusedPane,
            hover
        )

    fun paneIconContainer(shown: Boolean, focused: Boolean, hover: Boolean) =
        when {
            focused -> paneIconContainerFocused
            hover -> paneIconContainerHover
            shown -> paneIconContainerShown
            else -> paneIconContainerBase
        }

    val paneIconContainerBase = instructionsOf(
        size(width),
        margin { 6.dp },
        cornerRadius { 4.dp },
        alignItems.center
    )

    val paneIconContainerHover = paneIconContainerBase + backgrounds.lightOverlay

    val paneIconContainerShown = paneIconContainerBase + backgrounds.lightOverlay

    val paneIconContainerFocused = paneIconContainerBase + backgrounds.surfaceVariant

    val paneIconSeparator = instructionsOf(
        width { width },
        height { 16.dp },
        borderTop(colors.outline),
        margin(top = 8.dp, left = 8.dp, right = 8.dp, bottom = 7.dp)
    )

    val paneIcon = instructionsOf(
        fill(colors.onSurface)
    )

    val tooltipContainer = instructionsOf(
        paddingHorizontal { 16.dp },
        paddingVertical { 8.dp },
        backgrounds.reverse,
        cornerRadius { 4.dp },
        gap { 8.dp },
        //zIndex { 100 }
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
        borderBottom(toolBorderColor)
        //zIndex { 100 } // to have icon tooltips over items
    )

    val toolPaneTitleText = instructionsOf(
        textColors.onSurface,
        fontSize { 13.sp },
        semiBoldFont,
        noSelect,
        paddingTop { 3.dp } // for better visual experience
    )

    val toolPaneContainer = instructionsOf(
        rowTemplate(titleHeight, 1.fr),
        maxSize,
        toolBackground,
    )

    val toolPaneContent = instructionsOf(
        maxSize,
        scroll,
        padding { 8.dp }
    )

    val noContentContainer = instructionsOf(
        maxWidth,
        paddingTop { 32.dp },
        alignItems.center
    )

    val noContentText = instructionsOf(
        textMedium,
        textColors.onSurfaceVariant,
        noSelect
    )

}