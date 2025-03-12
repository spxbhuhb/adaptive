package `fun`.adaptive.ui.menu

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.variantColors


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
        size(20.dp, 20.dp),
        svgWidth(20.dp),
        svgHeight(20.dp)
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

    open fun itemColors(active: Boolean, hover: Boolean) =
        colors(active, hover)

    open val shortcut = instructionsOf(
        fontSize(13.sp),
        lineHeight(18.dp),
        paddingLeft(4.dp),
        normalFont,
        noSelect
    )

    open fun variantItemColors(active: Boolean, hover: Boolean) =
        variantColors(active, hover)

    companion object {
        var DEFAULT = ContextMenuTheme()
    }
}