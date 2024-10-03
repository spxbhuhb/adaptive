package `fun`.adaptive.ui.theme

import `fun`.adaptive.foundation.instruction.instructionsOf

fun colors(active: Boolean = false, hover: Boolean = false) =
    when {
        active -> primaryColors
        hover -> hoverColors
        else -> normalColors
    }

var primaryColors = instructionsOf(backgrounds.primary, textColors.onPrimary, iconColors.onPrimary)
var hoverColors = instructionsOf(backgrounds.primaryHover, textColors.onPrimaryHover, iconColors.onPrimaryHover)
var normalColors = instructionsOf(backgrounds.surface, textColors.onSurface, iconColors.onSurface)
