package `fun`.adaptive.ui.icon

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.colors

var iconTheme = IconTheme()

class IconTheme {
    val active = instructionsOf(
        svgFill(colors.primary),
        svgHeight(24.dp),
        svgWidth(24.dp),
        size(24.dp, 24.dp)
    )
}