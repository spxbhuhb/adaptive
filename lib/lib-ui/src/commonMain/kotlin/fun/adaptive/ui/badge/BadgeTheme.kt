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
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.colors

class BadgeTheme(
    backgroundAndBorder: Color,
    iconFill: Color,
    iconFillHover: Color,
    text: Color,
    val iconResource: GraphicsResourceSet?,
    cornerRadius: DPixel = 4.dp
) {

    val outerContainer = instructionsOf(
        height { 20.dp },
    )

    val iconContainer = instructionsOf(
        size(20.dp),
        alignItems.center,
        backgroundColor(backgroundAndBorder),
        cornerTopLeftRadius(cornerRadius),
        cornerBottomLeftRadius(cornerRadius),
        border(backgroundAndBorder, right = 0.dp)
    )

    val icon = instructionsOf(
        size(14.dp),
        svgWidth(14.dp),
        svgHeight(14.dp),
        fill(iconFill)
    )

    val textContainerBase = instructionsOf(
        height { 20.dp },
        paddingHorizontal { 8.dp },
        alignSelf.center
    )

    val textContainerStandalone = textContainerBase + instructionsOf(
        border(backgroundAndBorder),
        cornerRadius(cornerRadius)
    )

    val textContainerIcon = textContainerBase + instructionsOf(
        border(backgroundAndBorder, left = 0.dp),
        cornerRadius(0.dp, cornerRadius, 0.dp, cornerRadius)
    )

    val textContainerRemovable = textContainerBase + instructionsOf(
        border(backgroundAndBorder),
        cornerRadius (cornerRadius, 0.dp, cornerRadius, 0.dp)
    )

    val textContainerIconAndRemovable = textContainerBase + instructionsOf(
        border(backgroundAndBorder),
        cornerRadius(0.dp)
    )

    val text = instructionsOf(
        fontSize { 11.sp },
        fontWeight { 400 },
        paddingTop { 2.dp },
        alignSelf.center,
        textColor(text)
    )

    val removableContainer = instructionsOf(
        size(20.dp),
        alignItems.center,
        backgroundColor(backgroundAndBorder),
        cornerTopRightRadius(cornerRadius),
        cornerBottomRightRadius(cornerRadius),
        border(backgroundAndBorder, left = 0.dp)
    )

    val removableContainerHover = instructionsOf(
        size(20.dp),
        alignItems.center,
        backgroundColor(backgroundAndBorder.opaque(0.5)),
        cornerTopRightRadius(cornerRadius),
        cornerBottomRightRadius(cornerRadius),
        border(backgroundAndBorder, left = 0.dp)
    )

    val removableIcon = instructionsOf(
        size(12.dp),
        svgWidth(12.dp),
        svgHeight(12.dp),
        fill(iconFill)
    )

    val removableIconHover = instructionsOf(
        size(12.dp),
        svgWidth(12.dp),
        svgHeight(12.dp),
        fill(iconFillHover)
    )

    companion object {
        var success = BadgeTheme(
            backgroundAndBorder = colors.successSurface, 
            iconFill = colors.onSuccessSurface,
            iconFillHover = colors.onSurface,
            text = colors.onSurfaceVariant,
            iconResource = Graphics.success
        )

        var info = BadgeTheme(
            backgroundAndBorder = colors.info,
            iconFill = colors.onInfoSurface,
            iconFillHover = colors.onSurface,
            text = colors.onSurfaceVariant,
            iconResource = Graphics.info
        )

        var warning = BadgeTheme(
            backgroundAndBorder = colors.warningSurface,
            iconFill = colors.onWarningSurface,
            iconFillHover = colors.onSurface,
            text = colors.onSurface,
            iconResource = Graphics.warning
        )

        var error = BadgeTheme(
            backgroundAndBorder = colors.failSurface,
            iconFill = colors.onFailSurface,
            iconFillHover = colors.onSurface,
            text = colors.onSurface,
            iconResource = Graphics.error
        )

        var suppressed = BadgeTheme(
            backgroundAndBorder = colors.selectedSurfaceFocus,
            iconFill = colors.onSurface,
            iconFillHover = colors.onSurface,
            text = colors.onSurfaceVariant,
            iconResource = Graphics.info
        )

        var important = BadgeTheme(
            backgroundAndBorder = colors.importantSurface,
            iconFill = colors.onImportantSurface,
            iconFillHover = colors.onSurface,
            text = colors.onSurface,
            iconResource = Graphics.info
        )

        var default = suppressed

        val badgeThemeMap = mutableMapOf<String, BadgeTheme>()
    }

}