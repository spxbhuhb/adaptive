package `fun`.adaptive.ui.theme


import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.textColor

var colors = ThemeColors()

class ThemeColors {
    val surface = color(0xFFFFFF)
    val onSurface = color(0x1E1E1E)
    val onSurfaceFriendly = color(0x3CB371u)
    val onSurfaceAngry = color(0xEC5453u)

    val surfaceVariant = color(0xF2F2F2)
    val onSurfaceVariant = color(0x757575)

    val friendlySurface = color(0x3CB371u)
    val angrySurface = color(0xEC5453u)

    val primary = color(0x6259CE)
    val onPrimary = color(0xFFFFFF)

    val primaryHover = color(0x9B8CFFu)
    val onPrimaryHover = color(0xFFFFFFu)

    val outline = color(0xC5C5C5)
    val overlay = color(0x0, opacity = 0.4f)

    val danger = color(0xEC5453u)
    val onDanger = color(0xFFFFFFu)

    val white = color(0xFFFFFF)
}