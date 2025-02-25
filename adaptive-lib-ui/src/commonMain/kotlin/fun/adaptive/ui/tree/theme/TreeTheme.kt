package `fun`.adaptive.ui.tree.theme

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.lineHeight
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.normalFont
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.colors

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
        maxWidth
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

}