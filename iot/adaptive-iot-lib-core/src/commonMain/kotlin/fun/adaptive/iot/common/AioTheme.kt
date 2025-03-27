package `fun`.adaptive.iot.common

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.marginBottom
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.paddingHorizontal
import `fun`.adaptive.ui.api.tabIndex
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.value.item.AvStatus

class AioTheme {

    val itemListContainer = instructionsOf(
        maxSize,
        verticalScroll,
        borders.outline,
        paddingVertical { 12.dp },
        paddingHorizontal { 12.dp },
        cornerRadius { 8.dp },
        gap { 12.dp }
    )

    val itemListHeader = instructionsOf(
        height { 46.dp },
        gap { 12.dp },
        paddingHorizontal { 10.dp },
        cornerRadius { 10.dp },
        alignItems.startCenter,
        backgrounds.surface,
        border(colors.outline.opaque(0.5f), 1.dp),
        marginBottom { 12.dp }
    )

    val itemListItemContainer = instructionsOf(
        maxWidth,
        gap { 12.dp },
        height { 56.dp },
        paddingHorizontal { 10.dp },
        cornerRadius { 10.dp },
        alignItems.startCenter,
        backgrounds.surface,
        border(colors.outline.opaque(0.5f), 1.dp)
    )

    val itemListIconContainer = instructionsOf(
        alignItems.center,
        height { 36.dp },
        width { 36.dp },
        cornerRadius { 10.dp },
        backgrounds.successSurface
    )

    val itemListIcon = instructionsOf(
        width { 24.dp }, height { 24.dp }, svgHeight(24.dp), svgWidth(24.dp),
        fill(colors.onSuccessSurface)
    )

    val deviceSummary = instructionsOf(
        maxWidth,
        gap { 16.dp },
        height { 28.dp },
        colTemplate(80.dp, 1.fr, 160.dp, 84.dp),
        alignItems.startCenter
    )

    val pointSummary = instructionsOf(
        maxWidth,
        gap { 16.dp },
        height { 28.dp },
        colTemplate(1.fr, 1.fr, 160.dp, 84.dp),
        alignItems.startCenter
    )

    val statusContainer = instructionsOf(
        cornerRadius { 14.dp },
        height { 24.dp },
        width { 82.dp },
        paddingHorizontal { 10.dp },
        alignSelf.endCenter,
        alignItems.center
    )

    val statusText = instructionsOf(
        fontSize { 13.sp },
        normalFont
    )

    fun statusColor(status: AvStatus) =
        when {
            status.isOk -> colors.success
            else -> colors.fail
        }

    fun statusBorder(color: Color) =
        border(color.opaque(0.4f), 1.dp)

    val inlineEditorPopup = instructionsOf(
        backgrounds.surfaceVariant,
        borders.outline,
        padding { 16.dp },
        cornerRadius { 4.dp },
        onClick { it.stopPropagation() },
        tabIndex { 0 },
        zIndex { 10000 }
    )

    val singleHistoryColumns = colTemplate(160.dp, 1.fr, 84.dp, 84.dp)

    val historyListHeader = instructionsOf(
        height { 42.dp }, // depends marginBottom
        gap { 12.dp },
        paddingLeft { 20.dp },
        paddingRight { 4.dp },
        cornerRadius { 10.dp },
        alignItems.startCenter,
        backgrounds.surface,
        border(colors.outline.opaque(0.4f), 1.dp),
        maxWidth,
        marginBottom { 8.dp }
    )

    val historyRecordContainer = instructionsOf(
        maxWidth,
        gap { 12.dp },
        height { 34.dp },
        paddingLeft { 20.dp },
        paddingRight { 4.dp },
        cornerRadius { 10.dp },
        alignItems.startCenter,
        backgrounds.surface,
        border(colors.outline.opaque(0.4f), 1.dp),
        maxWidth,
    )

    companion object {
        val DEFAULT = AioTheme()
    }
}