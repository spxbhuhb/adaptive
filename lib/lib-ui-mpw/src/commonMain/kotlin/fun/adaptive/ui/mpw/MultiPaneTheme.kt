package `fun`.adaptive.ui.mpw

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.mpw.model.PaneId
import `fun`.adaptive.ui.splitpane.SplitPaneTheme
import `fun`.adaptive.ui.theme.*

class MultiPaneTheme(
    val width: DPixel = 40.dp,
    val dividerSize: DPixel = 9.dp,
    toolBackground: AdaptiveInstruction = backgrounds.surfaceVariant,
    val toolBorderColor: Color = colors.lightOutline
) : AbstractTheme() {

    companion object {
        var DEFAULT = MultiPaneTheme()
    }

    var splitPaneTheme = SplitPaneTheme(color = toolBorderColor)

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
        thisPane: PaneId,
        activePane: PaneId?,
        focusedPane: PaneId?,
        hover: Boolean
    ) =
        paneIconContainer(
            thisPane == activePane,
            thisPane == focusedPane,
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
        height { paneHeaderHeightDp },
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

    val toolPaneTitleActionContainer = instructionsOf(
        backgrounds.surfaceVariant
    )

    val toolPaneContainer = instructionsOf(
        rowTemplate(paneHeaderHeightDp, 1.fr),
        maxSize,
        toolBackground,
    )

    val toolPaneContent = instructionsOf(
        maxSize,
        scroll,
        padding { 8.dp }
    )

    val noContentContainer = instructionsOf(
        maxSize,
        paddingTop { 32.dp },
        alignItems.center
    )

    val noContentText = instructionsOf(
        textMedium,
        textColors.onSurfaceVariant,
        noSelect
    )

    val contentPaneContainer = instructionsOf(
        paddingHorizontal { 24.dp },
        paddingVertical { 16.dp },
        gap { 16.dp }
    )
}