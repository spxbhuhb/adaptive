package `fun`.adaptive.ui.badge

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors

class BadgeTheme {

    val outerContainer = instructionsOf(
        height { 20.dp },
    )

    val iconContainer = instructionsOf(
        size(20.dp),
        alignItems.center,
        backgroundColor(colors.selectedSurfaceFocus),
        cornerTopLeftRadius(4.dp),
        cornerBottomLeftRadius(4.dp),
        border(colors.selectedSurfaceFocus, right = 0.dp)
    )

    val icon = instructionsOf(
        size(14.dp),
        svgWidth(14.dp),
        svgHeight(14.dp),
        fill(colors.onSurface)
    )

    val textContainer = instructionsOf(
        height { 20.dp },
        paddingHorizontal { 8.dp },
        alignSelf.center,
        border(colors.selectedSurfaceFocus, left = 0.dp),
        cornerTopRightRadius(4.dp),
        cornerBottomRightRadius(4.dp)
    )

    val text = instructionsOf(
        fontSize { 11.sp },
        fontWeight { 400 },
        paddingTop { 2.dp },
        alignSelf.center,
        textColors.onSurfaceVariant
    )

    companion object {
        val default = BadgeTheme()
    }

}