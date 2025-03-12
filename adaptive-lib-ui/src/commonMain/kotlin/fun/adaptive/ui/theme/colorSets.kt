package `fun`.adaptive.ui.theme

import `fun`.adaptive.foundation.instruction.instructionsOf

fun colors(active: Boolean = false, hover: Boolean = false) =
    when {
        active -> primaryColors
        hover -> hoverColors
        else -> normalColors
    }

fun colorsSurface(active: Boolean = false, hover: Boolean = false) =
    when {
        active -> primaryColors
        hover -> hoverColors
        else -> normalColorsSurface
    }

fun variantColors(active: Boolean = false, hover: Boolean = false) =
    when {
        active -> primaryColors
        hover -> hoverColors
        else -> normalVariantColors
    }

fun handleColors(active: Boolean = false, hover: Boolean = false) =
    when {
        active -> primaryColors
        hover -> hoverColors
        else -> normalHandleColors
    }


var primaryColors = instructionsOf(backgrounds.primary, textColors.onPrimary, iconColors.onPrimary)
var hoverColors = instructionsOf(backgrounds.primaryHover, textColors.onPrimaryHover, iconColors.onPrimaryHover)
var normalColors = instructionsOf(textColors.onSurface, iconColors.onSurfaceFriendly)
var normalColorsSurface = instructionsOf(backgrounds.surface, textColors.onSurface, iconColors.onSurfaceFriendly)

var normalVariantColors = instructionsOf(textColors.onSurfaceVariant, iconColors.onSurfaceVariant)
var normalHandleColors = instructionsOf(textColors.onSurface, iconColors.onSurface)

