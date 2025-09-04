package `fun`.adaptive.ui.icon

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.AbstractTheme
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textSmall

var onSurfaceIconTheme = IconTheme(colors.onSurface)
var primaryIconTheme = IconTheme(colors.primary)

val tableIconTheme = IconTheme(
    colors.onSurface,
    iconSize = 24.dp,
    containerSize = 34.dp,
    margin = 3.dp,
    cornerRadius = 6.dp
)

val focusTableIconTheme = IconTheme(
    colors.primary,
    iconSize = 24.dp,
    containerSize = 34.dp,
    margin = 3.dp,
    cornerRadius = 6.dp
)

val denseIconTheme = IconTheme(
    colors.onSurface,
    iconSize = 22.dp,
    containerSize = 28.dp,
    margin = 2.dp,
    cornerRadius = 6.dp
)

val denseVariantIconTheme = IconTheme(
    colors.onSurfaceVariant,
    iconSize = 22.dp,
    containerSize = 28.dp,
    margin = 2.dp,
    cornerRadius = 6.dp
)

val smallCloseIconTheme = IconTheme(
    colors.onSurfaceVariant,
    iconSize = 13.dp,
    containerSize = 17.dp,
    margin = 1.dp,
    cornerRadius = (8.5).dp
)

val noContainerIconTheme = IconTheme(
    colors.onSurface,
    iconSize = 24.dp,
    containerSize = 24.dp,
    margin = 0.dp,
    cornerRadius = 0.dp
)

class IconTheme(
    val color: Color,
    val iconSize: DPixel = 24.dp,
    val containerSize: DPixel = 34.dp,
    val margin: DPixel = 0.dp,
    val cornerRadius: DPixel = 16.dp
) : AbstractTheme() {

    val icon = instructionsOf(
        fill(color),
        svgHeight(iconSize),
        svgWidth(iconSize),
        size(iconSize, iconSize)
    )

    val actionIcon = instructionsOf(
        svgHeight(iconSize),
        svgWidth(iconSize),
        size(iconSize, iconSize)
    )

    val actionIconContainer = instructionsOf(
        size(containerSize, containerSize),
        margin(margin),
        cornerRadius(cornerRadius),
        alignItems.center,
        tabIndex { inputTabIndex }
    )

    val tooltip = instructionsOf(
        paddingVertical { 4.dp },
        paddingHorizontal { 12.dp },
        //border(colors.outline, 1.dp),
        cornerRadius(4.dp),
        backgrounds.reverse,
        popupAlign.afterBelow,
        zIndex { 100 },
        // this is buggy - dropShadow(colors.reverse.opaque(0.2f), 4.dp, 4.dp, 4.dp),
    )

    val tooltipText = instructionsOf(
        textSmall,
        textColors.onReverse,
        noSelect
    )

    val focusBorder = border(colors.focusColor, 1.dp)

    val nonFocusBorder = border(colors.transparent, 1.dp)

    val nonHoverBackground = backgroundColor(colors.transparent)

    val hoverBackground = backgroundColor(colors.reverse.opaque(0.1))

    fun border(focus : Boolean) = instructionsOf(
       if (focus) focusBorder else nonFocusBorder
    )

    fun background(hover: Boolean) =
        if (hover) hoverBackground else nonHoverBackground

    val svgNonHoverColors = instructionsOf(
        fill(color),
    )

    val svgHoverColors = instructionsOf(
        fill(color)
    )

    fun svgColors(hover: Boolean) =
        if (hover) svgHoverColors else svgNonHoverColors

}