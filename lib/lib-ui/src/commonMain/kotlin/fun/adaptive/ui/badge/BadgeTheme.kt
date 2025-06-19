package `fun`.adaptive.ui.badge

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
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

open class BadgeTheme(
    val height: DPixel = 20.dp,
    cornerRadius: DPixel = 4.dp,
    border: Color,
    background: Color = colors.surface,
    textColor: Color,
    textInstructions : AdaptiveInstructionGroup = instructionsOf(fontSize { 11.sp }, fontWeight { 400 }),
    val iconResource: GraphicsResourceSet? = null,
    iconSize: DPixel = 14.dp,
    iconColor: Color,
    iconColorHover: Color,
    iconBackground: Color = border
) {

    var outerContainer = instructionsOf(
        height { height },
        cornerRadius(cornerRadius),
        backgroundColor(background)
    )

    var iconContainer = instructionsOf(
        size(height),
        alignItems.center,
        backgroundColor(iconBackground),
        cornerTopLeftRadius(cornerRadius),
        cornerBottomLeftRadius(cornerRadius),
        border(border, right = 0.dp)
    )

    var icon = instructionsOf(
        size(iconSize),
        svgWidth(iconSize),
        svgHeight(iconSize),
        fill(iconColor)
    )

    var textContainerBase = instructionsOf(
        height { height },
        alignSelf.center,
        paddingHorizontal { 8.dp }
    )

    var textContainerStandalone = textContainerBase + instructionsOf(
        border(border),
        cornerRadius(cornerRadius)
    )

    var textContainerIcon = textContainerBase + instructionsOf(
        border(border, left = 0.dp),
        cornerRadius(0.dp, cornerRadius, 0.dp, cornerRadius)
    )

    var textContainerRemovable = textContainerBase + instructionsOf(
        border(border),
        cornerRadius(cornerRadius, 0.dp, cornerRadius, 0.dp)
    )

    var textContainerIconAndRemovable = textContainerBase + instructionsOf(
        border(border),
        cornerRadius(0.dp)
    )

    var text = textInstructions + instructionsOf(
        paddingTop { 2.dp },
        alignSelf.center,
        textColor(textColor)
    )

    var removableContainer = instructionsOf(
        size(height),
        alignItems.center,
        backgroundColor(border),
        cornerTopRightRadius(cornerRadius),
        cornerBottomRightRadius(cornerRadius),
        border(border, left = 0.dp)
    )

    var removableContainerHover = instructionsOf(
        size(height),
        alignItems.center,
        backgroundColor(border.opaque(0.5)),
        cornerTopRightRadius(cornerRadius),
        cornerBottomRightRadius(cornerRadius),
        border(border, left = 0.dp)
    )

    var removableIcon = instructionsOf(
        size(iconSize - 2.dp),
        svgWidth(iconSize - 2.dp),
        svgHeight(iconSize - 2.dp),
        fill(iconColor)
    )

    var removableIconHover = instructionsOf(
        size(iconSize - 2.dp),
        svgWidth(iconSize - 2.dp),
        svgHeight(iconSize - 2.dp),
        fill(iconColorHover)
    )

    companion object {
        var success = BadgeTheme(
            border = colors.successSurface,
            textColor = colors.onSurface,
            iconResource = Graphics.success,
            iconColor = colors.onSuccessSurface,
            iconColorHover = colors.onSurface
        )

        var info = BadgeTheme(
            border = colors.info,
            textColor = colors.onSurface,
            iconResource = Graphics.info,
            iconColor = colors.onInfoSurface,
            iconColorHover = colors.onSurface
        )

        var warning = BadgeTheme(
            border = colors.warningSurface,
            textColor = colors.onSurface,
            iconResource = Graphics.warning,
            iconColor = colors.onWarningSurface,
            iconColorHover = colors.onSurface
        )

        var error = BadgeTheme(
            border = colors.failSurface,
            textColor = colors.onSurface,
            iconResource = Graphics.error,
            iconColor = colors.onFailSurface,
            iconColorHover = colors.onSurface
        )

        var suppressed = BadgeTheme(
            border = colors.selectedSurfaceFocus,
            textColor = colors.onSurfaceVariant,
            iconResource = Graphics.info,
            iconColor = colors.onSurfaceVariant,
            iconColorHover = colors.onSurface
        )

        var important = BadgeTheme(
            border = colors.importantSurface,
            textColor = colors.onSurface,
            iconResource = Graphics.info,
            iconColor = colors.onImportantSurface,
            iconColorHover = colors.onSurface
        )

        var default = suppressed

        val badgeThemeMap = mutableMapOf<String, BadgeTheme>()
    }

}