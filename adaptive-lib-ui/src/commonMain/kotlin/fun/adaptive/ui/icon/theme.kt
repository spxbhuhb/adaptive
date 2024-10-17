package `fun`.adaptive.ui.icon

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.colors

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
}