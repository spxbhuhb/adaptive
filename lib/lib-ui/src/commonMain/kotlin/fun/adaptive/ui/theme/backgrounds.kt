package `fun`.adaptive.ui.theme

import `fun`.adaptive.ui.api.backgroundColor

var backgrounds = ThemeBackgrounds()

class ThemeBackgrounds {
    val surface = backgroundColor(colors.surface)
    val surfaceVariant = backgroundColor(colors.surfaceVariant)
    val primary = backgroundColor(colors.primary)
    val selected = backgroundColor(colors.selectedSurfaceNoFocus)
    val overlay = backgroundColor(colors.overlay)
    val lightOverlay = backgroundColor(colors.lightOverlay)
    val primaryHover = backgroundColor(colors.primaryHover)
    val surfaceHover = backgroundColor(colors.surface.opaque(0.1f))
    val friendly = backgroundColor(colors.successSurface)
    val friendlyOpaque = backgroundColor(colors.onSurfaceFriendly.opaque(0.2f))

    val angry = backgroundColor(colors.failSurface)
    val reverse = backgroundColor(colors.reverse)

    val successSurface = backgroundColor(colors.successSurface)
    val infoSurface = backgroundColor(colors.infoSurface)
    val warningSurface = backgroundColor(colors.warningSurface)
    val failSurface = backgroundColor(colors.failSurface)
}