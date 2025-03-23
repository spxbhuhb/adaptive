package `fun`.adaptive.iot.common

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
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

    val deviceSummary = instructionsOf(
        maxWidth,
        gap { 16.dp },
        height { 28.dp },
        colTemplate(80.dp, 1.fr, 160.dp, 60.dp),
        alignItems.startCenter
    )

    val pointSummary = instructionsOf(
        maxWidth,
        gap { 16.dp },
        height { 28.dp },
        colTemplate(1.fr, 160.dp, 60.dp),
        alignItems.startCenter
    )

    val statusContainer = instructionsOf(
        cornerRadius { 8.dp },
        height { 28.dp },
        paddingHorizontal { 10.dp },
        alignSelf.endCenter,
        alignItems.center
    )

    val statusText = instructionsOf(
        fontSize { 14.sp },
        semiBoldFont
    )

    fun statusColor(status: AvStatus) =
        when {
            status.isOk -> colors.success
            else -> colors.fail
        }

    fun statusBorder(color: Color) =
        border(color, 2.dp)

    companion object {
        val DEFAULT = AioTheme()
    }
}