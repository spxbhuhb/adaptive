package `fun`.adaptive.ui.icon

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.api.popupAlign
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textSmall

var onSurfaceIconTheme = IconTheme(colors.onSurface)
var primaryIconTheme = IconTheme(colors.primary)

val tableIconTheme = IconTheme(
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

class IconTheme(
    val color: Color,
    val iconSize: DPixel = 24.dp,
    val containerSize: DPixel = 34.dp,
    val margin: DPixel = 0.dp,
    val cornerRadius: DPixel = 16.dp
) {
    val icon = instructionsOf(
        svgFill(color),
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
        tabIndex { 0 }
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
        textColors.onReverse
    )

    val nonHoverBackground = backgroundColor(colors.surface.opaque(0f))

    val hoverBackground = backgroundColor(colors.primary.opaque(0.2f))

    fun background(hover: Boolean) =
        if (hover) hoverBackground else nonHoverBackground

    val svgNonHoverColors = instructionsOf(
        svgFill(color),
    )

    val svgHoverColors = instructionsOf(
        svgFill(color)
    )

    fun svgColors(hover: Boolean) =
        if (hover) svgHoverColors else svgNonHoverColors

}