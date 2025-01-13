package `fun`.adaptive.ui.tree.theme

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.lineHeight
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.colors

open class TreeTheme(
    open val width: DPixel = 314.dp,
    open val itemHeight: DPixel = 32.dp,
    open val indent: DPixel = 16.dp,
) {
    open val container = instructionsOf(
        alignItems.startCenter,
        width { width },
    )

    open val icon = instructionsOf(
        size(24.dp, 24.dp)
    )

    open val item = instructionsOf(
        height(itemHeight),
        alignItems.startCenter,
        gap(8.dp)
    )

    open val label = instructionsOf(
        fontSize(16.sp),
        lineHeight(22.dp),
        noSelect
    )

    open fun itemColors(active: Boolean, hover: Boolean) =
        colors(active, hover)

}