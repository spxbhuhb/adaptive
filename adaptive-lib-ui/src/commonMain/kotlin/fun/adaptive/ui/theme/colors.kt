package `fun`.adaptive.ui.theme


import `fun`.adaptive.ui.api.color

var colors = ThemeColors()

class ThemeColors {

    val success = color(0x3CB371u)
    val info = color(0x4A7ACFu)
    val warning = color(0xFFE066u)
    val fail = color(0xC43E3Du)
    val friendly = color(0x3CB371u)
    val angry = color(0xEC5453u)
    val inactive = color(0xA2A6B8u)

    val surface = color(0xFFFFFF)
    val onSurface = color(0x1E1E1E)
    val onSurfaceFriendly = friendly
    val onSurfaceAngry = angry

    val surfaceVariant = color(0xF4F6F9)
    val onSurfaceVariant = color(0x757575)

    val successSurface = success
    val onSuccessSurface = color(0xFFFFFFu)

    val infoSurface = info
    val onInfoSurface = color(0xFFFFFFu)

    val warningSurface = warning
    val onWarningSurface = color(0x1E1E1Eu)

    val failSurface = fail
    val onFailSurface = color(0xFFFFFFu)

    val primary = color(0x0D6EFD)
    val onPrimary = color(0xFFFFFF)

    val primaryHover = color(0x9B8CFFu)
    val onPrimaryHover = color(0xFFFFFFu)

    val selectedSurfaceFocus = color(0xCBDAFE)
    val selectedSurfaceNoFocus = color(0xD7DADF)
    val hoverSurface = color(0xCBDAFE)

    val outline = color(0xC5C5C5)
    val lightOutline = color(0xE0E0E0)
    val overlay = color(0x0, opacity = 0.4f)
    val lightOverlay = color(0x0, opacity = 0.1f)

    val danger = color(0xEC5453u)
    val onDanger = color(0xFFFFFFu)

    val white = color(0xFFFFFF)

    val reverse = color(0x1E1E1E)
    val onReverse = color(0xFFFFFF)
    val onReverseVariant = color(0xBBBBBB)

    val focusColor = color(0x0D6EFD)

}