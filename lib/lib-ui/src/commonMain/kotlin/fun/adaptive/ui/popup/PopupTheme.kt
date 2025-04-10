package `fun`.adaptive.ui.popup

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.*

class PopupTheme {

    val inlineEditorPopup = instructionsOf(
        marginTop { 16.dp },
        backgrounds.surfaceVariant,
        borders.outline,
        padding { 16.dp },
        cornerRadius { 4.dp },
        onClick { it.stopPropagation() },
        tabIndex { 0 },
        zIndex { 10000 }
    )

    val modalContainer = instructionsOf(
        backgrounds.surfaceVariant,
        borders.outline,
        cornerRadius(8.dp),
        dropShadow(colors.overlay, 16.dp, 16.dp, 16.dp),
    )

    val modalTitleHeight = 28.dp

    val modalTitleContainer = instructionsOf(
        colTemplate(28.dp, 1.fr),
        rowTemplate(28.dp),
        height(modalTitleHeight),
        cornerRadius { 8.dp }
    )

    val modalTitleText = instructionsOf(
        textColors.onSurfaceMedium,
        fontSize { 13.sp },
        semiBoldFont,
        alignSelf.center
    )

    val modalTitleIcon = instructionsOf(
        size(16.dp, 16.dp),
        iconColors.onSurface,
        svgWidth(16.dp),
        svgHeight(16.dp),
        alignSelf.center,
    )

    val modalButtonsHeight = 52.dp

    val modalButtons = instructionsOf(
        maxWidth,
        alignItems.end,
        borderTop(colors.lightOutline),
        paddingVertical { 12.dp },
        paddingRight { 16.dp },
        gap { 12.dp },
        height { modalButtonsHeight }
    )

    val feedbackContainer = instructionsOf(
        paddingVertical { 4.dp },
        paddingHorizontal { 12.dp },
        //border(colors.outline, 1.dp),
        cornerRadius(4.dp),
        backgrounds.reverse,
        popupAlign.afterBelow,
        zIndex { 100 },
        // this is buggy - dropShadow(colors.reverse.opaque(0.2f), 4.dp, 4.dp, 4.dp),
    )

    val feedbackText = instructionsOf(
        textSmall,
        textColors.onReverse,
        noSelect
    )

    companion object {
        var default = PopupTheme()
    }
}