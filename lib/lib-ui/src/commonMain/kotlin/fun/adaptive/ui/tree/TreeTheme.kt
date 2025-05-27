package `fun`.adaptive.ui.tree

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.arrow_drop_down
import `fun`.adaptive.ui.generated.resources.arrow_right
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.background
import `fun`.adaptive.ui.theme.foreground

open class TreeTheme(
    open val itemHeight: DPixel = 26.dp,
    open val indent: DPixel = 22.dp,
    open val itemPadding : DPixel = 4.dp,
) {
    open val container = instructionsOf(
        tabIndex { 0 },
        alignItems.startCenter
    )

    open val icon = instructionsOf(
        size(18.dp, 18.dp),
        svgWidth(18.dp),
        svgHeight(18.dp)
    )

    open val item = instructionsOf(
        height(itemHeight),
        alignItems.startCenter,
        cornerRadius { 4.dp }
    )

    open val label = instructionsOf(
        fontSize(13.sp),
        lineHeight(18.dp),
        paddingLeft(itemPadding),
        normalFont,
        noSelect
    )

    open fun itemBackground(selected: Boolean, focus: Boolean) =
        background(selected, focus = focus, variant = true)

    open fun itemForeground(selected: Boolean, focus: Boolean) =
        foreground(selected, focus = focus)

    open val handleContainer = instructionsOf(
        size(24.dp, 24.dp)
    )

    open val handleIcon = instructionsOf(
        cornerRadius { 4.dp }
    )

    open val handleIconOpen = Graphics.arrow_drop_down

    open val handleIconClosed = Graphics.arrow_right

    open fun itemHandleColors(selected : Boolean, focus: Boolean) =
        fill(foreground(selected, focus = focus).color)

    companion object {
        var DEFAULT = TreeTheme()
        var list = TreeTheme(indent = 0.dp, itemPadding = 8.dp)
    }
}