package `fun`.adaptive.ui.theme

import `fun`.adaptive.ui.api.textColor

var textColors = ThemeTextColors()

class ThemeTextColors {

    val success = textColor(colors.success)
    val info = textColor(colors.info)
    val warning = textColor(colors.warning)
    val fail = textColor(colors.fail)

    val focus = textColor(colors.focusColor)

    val onSurface = textColor(colors.onSurface)
    val onSurfaceMedium = textColor(colors.onSurfaceMedium)
    val onSurfaceVariant = textColor(colors.onSurfaceVariant)
    val onSurfaceFriendly = textColor(colors.onSurfaceFriendly)
    val onSurfaceAngry = textColor(colors.onSurfaceAngry)
    val onPrimary = textColor(colors.onPrimary)
    val onPrimaryHover = textColor(colors.onPrimaryHover)
    val onSelected = textColor(colors.onSurface)

    val primary = textColor(colors.primary)
    val white = textColor(colors.white)

    val onSuccessSurface = textColor(colors.onSuccessSurface)
    val onInfoSurface = textColor(colors.onInfoSurface)
    val onWarningSurface = textColor(colors.onWarningSurface)
    val onFailSurface = textColor(colors.onFailSurface)

    val onReverse = textColor(colors.onReverse)
    val onReverseVariant = textColor(colors.onReverseVariant)

}