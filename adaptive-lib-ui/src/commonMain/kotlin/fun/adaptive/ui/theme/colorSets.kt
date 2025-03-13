package `fun`.adaptive.ui.theme

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.instruction.decoration.BackgroundColor
import `fun`.adaptive.ui.instruction.text.TextColor

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

fun background(
    selected: Boolean,
    inactive: Boolean = false,
    variant: Boolean = false,
    focus: Boolean = false,
    hover: Boolean = false
) =
    BackgroundColor(
        when {
            inactive -> if (variant) colors.surfaceVariant else colors.surface
            selected -> if (focus) colors.selectedSurfaceFocus else colors.selectedSurfaceNoFocus
            hover -> colors.hoverSurface
            else -> if (variant) colors.surfaceVariant else colors.surface
        }
    )

fun foreground(
    selected: Boolean,
    inactive: Boolean = false,
    variant: Boolean = false,
    focus: Boolean = false,
    hover: Boolean = false,
) =
    TextColor(
        when {
            inactive -> colors.inactive
            selected -> if (focus) colors.onSurface else colors.onSurface
            hover -> colors.onSurface
            else -> if (variant) colors.onSurfaceVariant else colors.onSurface
        }
    )


var primaryColors = instructionsOf(backgrounds.primary, textColors.onPrimary, iconColors.onPrimary)
var hoverColors = instructionsOf(backgrounds.primaryHover, textColors.onPrimaryHover, iconColors.onPrimaryHover)
var normalColors = instructionsOf(textColors.onSurface, iconColors.onSurfaceFriendly)
var normalColorsSurface = instructionsOf(backgrounds.surface, textColors.onSurface, iconColors.onSurfaceFriendly)

var normalVariantColors = instructionsOf(textColors.onSurfaceVariant, iconColors.onSurfaceVariant)
var normalHandleColors = instructionsOf(textColors.onSurface, iconColors.onSurface)

