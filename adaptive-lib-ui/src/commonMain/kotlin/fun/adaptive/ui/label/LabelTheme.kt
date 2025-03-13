package `fun`.adaptive.ui.label

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.icon.IconTheme
import `fun`.adaptive.ui.icon.denseIconTheme
import `fun`.adaptive.ui.icon.smallCloseIconTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textSmall

class LabelTheme {

    val inputLabel = instructionsOf(
        textColors.onSurfaceVariant, textSmall
    )

    val uuidLabelContainer = instructionsOf(
        alignItems.center,
        gap { 4.dp }
    )

    val uuidLabelText = instructionsOf(
        textColors.onSurfaceVariant, textSmall
    )

    val copyIconTheme = IconTheme(
        colors.onSurfaceVariant,
        iconSize = 16.dp,
        containerSize = 20.dp,
        margin = 0.dp,
        cornerRadius = 3.dp
    )

    companion object {
        var DEFAULT = LabelTheme()
    }
}