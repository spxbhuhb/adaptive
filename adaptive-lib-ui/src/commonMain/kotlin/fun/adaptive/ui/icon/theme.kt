package `fun`.adaptive.ui.icon

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.decoration.CornerRadius
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.colors

var onSurfaceIconTheme = IconTheme(colors.onSurface)
var primaryIconTheme = IconTheme(colors.primary)

val tableIconTheme = IconTheme(
    colors.primary,
    iconSize = 24.dp,
    containerSize = 34.dp,
    margin = 2.dp,
    cornerRadius = 15.dp
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
        alignItems.center
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