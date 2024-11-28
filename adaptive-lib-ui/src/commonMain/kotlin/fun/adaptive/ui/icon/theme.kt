package `fun`.adaptive.ui.icon

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.iconColors

var onSurfaceIconTheme = IconTheme(colors.onSurface)
var primaryIconTheme = IconTheme(colors.primary)

class IconTheme(
    val color : Color
){
    val icon = instructionsOf(
        svgFill(color),
        svgHeight(24.dp),
        svgWidth(24.dp),
        size(24.dp, 24.dp)
    )

    val actionIcon = instructionsOf(
        svgHeight(24.dp),
        svgWidth(24.dp),
        size(24.dp, 24.dp)
    )

    val actionIconContainer = instructionsOf(
        size(34.dp, 34.dp),
        cornerRadius(16.dp)
    )

    val nonHoverColors = instructionsOf(
        svgFill(color),
        backgroundColor(colors.surface)
    )

    val hoverColors = instructionsOf(
        iconColors.onPrimary,
        backgrounds.primary
    )

    fun colors(hover : Boolean) =
        if (hover) hoverColors else nonHoverColors

}