package `fun`.adaptive.ui.label

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.IconTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textSmall

class LabelTheme {

    val base = instructionsOf(
        fontSize(13.sp),
        paddingBottom { 2.dp }
    )

    val enabled = base + instructionsOf(
        textColors.onSurface
    )

    val focused = base + instructionsOf(
        textColors.primary
    )

    val disabled = base + instructionsOf(
        textColors.onSurfaceVariant
    )

    val invalidFocused = base + instructionsOf(
        textColors.fail
    )

    val invalidNotFocused = base + instructionsOf(
        textColors.fail
    )

    val uuidLabelContainer = instructionsOf(
        alignItems.center,
        gap { 4.dp }
    )

    val uuidLabelText = instructionsOf(
        textColors.onSurfaceVariant,
        textSmall
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