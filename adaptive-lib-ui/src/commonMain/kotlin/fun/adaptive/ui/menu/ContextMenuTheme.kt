package `fun`.adaptive.ui.menu

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.background
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.foreground


open class ContextMenuTheme(
    open val itemHeight: DPixel = 26.dp,
    open val indent: DPixel = 22.dp,
) {
    open val container = instructionsOf(
        alignItems.startCenter,
        width { 300.dp },
        padding { 6.dp },
        cornerRadius { 8.dp },
        border(colors.onSurface, 0.5.dp),
        backgrounds.surface,
        zIndex { 200 }
    )

    open val icon = instructionsOf(
        size(18.dp, 18.dp),
        svgWidth(18.dp),
        svgHeight(18.dp)
    )

    open val item = instructionsOf(
        height(itemHeight),
        alignItems.startCenter,
        maxWidth,
        cornerRadius { 4.dp },
        paddingHorizontal { 6.dp }
    )

    open val label = instructionsOf(
        fontSize(13.sp),
        lineHeight(18.dp),
        paddingLeft(8.dp),
        normalFont,
        noSelect
    )

    open fun itemBackground(hover: Boolean) =
        background(false, hover = hover, variant = false)

    open fun itemForeground(hover: Boolean, variant: Boolean) =
        foreground(false, hover = hover, variant = variant)

    open val shortcut = instructionsOf(
        fontSize(13.sp),
        lineHeight(18.dp),
        paddingLeft(4.dp),
        normalFont,
        noSelect
    )

    companion object {
        var DEFAULT = ContextMenuTheme()
    }
}