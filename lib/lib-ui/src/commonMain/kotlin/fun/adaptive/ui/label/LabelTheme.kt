package `fun`.adaptive.ui.label

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.IconTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.PopupAlign
import `fun`.adaptive.ui.instruction.layout.SizeStrategy
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
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

    /**
     * Applied when the label is to the right of the value (boolean input, for example).
     */
    val rightLabel = instructionsOf(
        padding(1.dp, 0.dp, 0.dp, 8.dp)// FIXME base vs right label in LabelTheme
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

    val infoIconTheme = IconTheme(
        colors.onSurface,
        iconSize = 16.dp,
        containerSize = 20.dp,
        margin = 0.dp,
        cornerRadius = 3.dp
    )

    val infoPopup = instructionsOf(
        focusFirst,
        tabIndex { 0 },
        padding { 12.dp },
        cornerRadius { 8.dp },
        border(colors.onSurface, 0.5.dp),
        backgrounds.surface,
        dropShadow(colors.overlay, 5.dp, 5.dp, 5.dp),
        zIndex { 200 },
        fillStrategy.resizeToMax,
        SizeStrategy(minWidth = 200.dp),
        PopupAlign.belowStart
    )

    companion object {
        var DEFAULT = LabelTheme()
    }
}