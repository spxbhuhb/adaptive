package `fun`.adaptive.ui.theme

import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.ui.api.textColor

var iconColors = ThemeIconColors()

class ThemeIconColors {
    val primary = fill(colors.primary)
    val onSurface = fill(colors.onSurface)
    val onSurfaceVariant = fill(colors.onSurfaceVariant)
    val onSurfaceFriendly = fill(colors.onSurfaceFriendly)
    val onSurfaceAngry = fill(colors.onSurfaceAngry)
    val onPrimary = fill(colors.onPrimary)
    val onPrimaryHover = fill(colors.onPrimaryHover)

    val onSelected = fill(colors.onSurface)
    val onFocusSurface = fill(colors.onFocusSurface)

    val onSuccessSurface = fill(colors.onSuccessSurface)
    val onInfoSurface = fill(colors.onInfoSurface)
    val onWarningSurface = fill(colors.onWarningSurface)
    val onFailSurface = fill(colors.onFailSurface)

}