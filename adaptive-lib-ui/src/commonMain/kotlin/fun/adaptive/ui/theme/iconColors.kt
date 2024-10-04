package `fun`.adaptive.ui.theme

import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.ui.api.textColor

var iconColors = ThemeIconColors()

class ThemeIconColors {
    val primary = svgFill(colors.primary)
    val onSurface = svgFill(colors.onSurface)
    val onSurfaceVariant = svgFill(colors.onSurfaceVariant)
    val onSurfaceFriendly = svgFill(colors.onSurfaceFriendly)
    val onSurfaceAngry = svgFill(colors.onSurfaceAngry)
    val onPrimary = svgFill(colors.onPrimary)
    val onPrimaryHover = svgFill(colors.onPrimaryHover)
}