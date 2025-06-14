package `fun`.adaptive.ui.badge

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.error
import `fun`.adaptive.ui.generated.resources.info
import `fun`.adaptive.ui.generated.resources.success
import `fun`.adaptive.ui.generated.resources.warning
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.value.AvStatus

class BadgeTheme(
    backgroundAndBorder: Color,
    iconFill: Color,
    text: Color,
    val iconResource: GraphicsResourceSet?
) {

    val outerContainer = instructionsOf(
        height { 20.dp },
    )

    val iconContainer = instructionsOf(
        size(20.dp),
        alignItems.center,
        backgroundColor(backgroundAndBorder),
        cornerTopLeftRadius(4.dp),
        cornerBottomLeftRadius(4.dp),
        border(backgroundAndBorder, right = 0.dp)
    )

    val icon = instructionsOf(
        size(14.dp),
        svgWidth(14.dp),
        svgHeight(14.dp),
        fill(iconFill)
    )

    val textContainer = instructionsOf(
        height { 20.dp },
        paddingHorizontal { 8.dp },
        alignSelf.center,
        border(backgroundAndBorder, left = 0.dp),
        cornerTopRightRadius(4.dp),
        cornerBottomRightRadius(4.dp)
    )

    val text = instructionsOf(
        fontSize { 11.sp },
        fontWeight { 400 },
        paddingTop { 2.dp },
        alignSelf.center,
        textColor(text)
    )

    companion object {
        val success = BadgeTheme(colors.successSurface, colors.onSuccessSurface, colors.onSurfaceVariant, Graphics.success)
        val info = BadgeTheme(colors.selectedSurfaceFocus, colors.onSurface, colors.onSurfaceVariant, Graphics.info)
        val warning = BadgeTheme(colors.warningSurface, colors.onWarningSurface, colors.onSurface, Graphics.warning)
        val error = BadgeTheme(colors.failSurface, colors.onFailSurface, colors.onSurface, Graphics.error)
        val important = BadgeTheme(colors.importantSurface, colors.onImportantSurface, colors.onSurface, Graphics.info)

        val default = info

        val badgeThemeMap = mutableMapOf<AvStatus, BadgeTheme>()
    }

}