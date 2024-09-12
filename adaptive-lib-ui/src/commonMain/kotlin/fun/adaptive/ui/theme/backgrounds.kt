package `fun`.adaptive.ui.theme

import `fun`.adaptive.ui.api.backgroundColor

var backgrounds = ThemeBackgrounds()

class ThemeBackgrounds {
    val surface = backgroundColor(colors.surface)
    val surfaceVariant = backgroundColor(colors.surfaceVariant)
    val primary = backgroundColor(colors.primary)
    val overlay = backgroundColor(colors.overlay)
}