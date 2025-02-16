package `fun`.adaptive.ui.theme


import `fun`.adaptive.ui.api.color

var colors = ThemeColors()

class ThemeColors {
    val surface = color(0xFFFFFF)
    val onSurface = color(0x1E1E1E)
    val onSurfaceFriendly = color(0x3CB371u)
    val onSurfaceAngry = color(0xEC5453u)

    val surfaceVariant = color(0xF2F2F2)
    val onSurfaceVariant = color(0x757575)

    val successSurface = color(0x3CB371u)
    val onSuccessSurface = color(0xFFFFFFu)

    val infoSurface = color(0x757575u)
    val onInfoSurface = color(0xFFFFFFu)

    val warningSurface = color(0xFFE066u)
    val onWarningSurface = color(0x1E1E1Eu)

    val failSurface = color(0xC43E3Du)
    val onFailSurface = color(0xFFFFFFu)

    val primary = color(0x6259CE)
    val onPrimary = color(0xFFFFFF)

    val primaryHover = color(0x9B8CFFu)
    val onPrimaryHover = color(0xFFFFFFu)

    val selected = color(0x6259CE)
    val onSelected = color(0xFFFFFF)

    val outline = color(0xC5C5C5)
    val overlay = color(0x0, opacity = 0.4f)

    val danger = color(0xEC5453u)
    val onDanger = color(0xFFFFFFu)

    val white = color(0xFFFFFF)

    val reverse = color(0x1E1E1E)
    val onReverse = color(0xFFFFFF)
    val onReverseVariant = color(0xBBBBBB)
}