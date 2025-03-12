package `fun`.adaptive.ui.theme

import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.instruction.dp

var borders = ThemeBorders()

class ThemeBorders {
    val onSurface = border(colors.onSurface, 1.dp)
    val primary = border(colors.primary, 1.dp)
    val outline = border(colors.outline, 1.dp)
    val friendly = border(colors.onSurfaceFriendly, 1.dp)
}