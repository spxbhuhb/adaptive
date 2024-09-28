package `fun`.adaptive.ui.theme

import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.ui.api.textColor

var iconColors = ThemeIconColors()

class ThemeIconColors {
    val onSurface = svgFill(colors.onSurface)
    val onSurfaceVariant = svgFill(colors.onSurfaceVariant)
    val onPrimary = svgFill(colors.onPrimary)
    val onPrimaryHover = svgFill(colors.onPrimaryHover)
}