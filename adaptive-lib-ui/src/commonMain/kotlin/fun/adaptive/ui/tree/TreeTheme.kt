package `fun`.adaptive.ui.tree

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.arrow_drop_down
import `fun`.adaptive.ui.builtin.arrow_right
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.handleColors

open class TreeTheme(
    open val itemHeight: DPixel = 26.dp,
    open val indent: DPixel = 22.dp,
) {
    open val container = instructionsOf(
        alignItems.startCenter,
        maxWidth
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
        cornerRadius { 4.dp }
    )

    open val label = instructionsOf(
        fontSize(13.sp),
        lineHeight(18.dp),
        paddingLeft(4.dp),
        normalFont,
        noSelect
    )

    open fun itemColors(active: Boolean, hover: Boolean) =
        colors(active, hover)

    open val handleContainer = instructionsOf(
        size(24.dp, 24.dp)
    )

    open val handleIcon = instructionsOf(
        cornerRadius { 4.dp }
    )

    open val handleIconOpen = Graphics.arrow_drop_down

    open val handleIconClosed = Graphics.arrow_right

    open fun itemHandleColors(active: Boolean, hover: Boolean) =
        handleColors(active, hover)

    companion object {
        var DEFAULT = TreeTheme()
    }
}