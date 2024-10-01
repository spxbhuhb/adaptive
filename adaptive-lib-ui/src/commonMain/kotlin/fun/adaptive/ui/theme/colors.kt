package `fun`.adaptive.ui.theme


import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.color

var colors = ThemeColors()

class ThemeColors {
    val surface = color(0xFFFFFF)
    val onSurface = color(0x1E1E1E)
    val surfaceVariant = color(0xF2F2F2)
    val onSurfaceVariant = color(0x757575)
    val primary = color(0x6259CE)
    val primaryHover = color(0x9B8CFFu)
    val onPrimary = color(0xFFFFFF)
    val onPrimaryHover = color(0xFFFFFFu)
    val outline = color(0xC5C5C5)
    val overlay = color(0x88888840u)
    val danger = color(0xEC5453u)
    val onDanger = color(0xFFFFFFu)
}

fun colors(active: Boolean = false, hover: Boolean = false) =
    when {
        active -> primaryColors
        hover -> hoverColors
        else -> normalColors
    }

var primaryColors = instructionsOf(backgrounds.primary, textColors.onPrimary, iconColors.onPrimary)
var hoverColors = instructionsOf(backgrounds.primaryHover, textColors.onPrimaryHover, iconColors.onPrimaryHover)
var normalColors = instructionsOf(backgrounds.surface, textColors.onSurface, iconColors.onSurface)
