package `fun`.adaptive.ui.navigation.sidebar.theme

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.fontWeight
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.lineHeight
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors

open class ThinSidebarTheme(
    open val width : DPixel = 80.dp,
    open val itemHeight : DPixel = 56.dp
){
    open val container = instructionsOf(
        width(width),
        gap(12.dp),
        padding(12.dp),
        backgrounds.surface
    )

    open val iconContainer = instructionsOf(
        size(56.dp, 32.dp),
        alignItems.center,
        cornerRadius(16.dp)
    )

    open val icon = instructionsOf(
        size(24.dp, 24.dp)
    )

    open val item = instructionsOf(
        width((width.value - 24).dp),
        height(itemHeight),
        alignItems.center,
        gap(4.dp)
    )

    open val label = instructionsOf(
        fontSize(12.sp),
        noSelect
    )

    val activeLabel = label + instructionsOf(
        fontWeight(500)
    )

    open fun icon(active: Boolean, hover: Boolean) =
        colors(active, hover)

    open fun label(active: Boolean, hover: Boolean) =
        if (active) activeLabel else label
}