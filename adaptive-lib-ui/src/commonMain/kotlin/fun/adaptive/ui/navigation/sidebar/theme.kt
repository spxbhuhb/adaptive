package `fun`.adaptive.ui.navigation.sidebar

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.borderRight
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.marginRight
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.hoverColors
import `fun`.adaptive.ui.theme.normalColors

var sideBarTheme = SideBarTheme()

open class SideBarTheme(
    open val width : DPixel = 314.dp,
    open val itemHeight : DPixel = 63.dp
){
    open val container = instructionsOf(

    )

    open val icon = instructionsOf(
        size(24.dp, 24.dp)
    )

    open val item = instructionsOf(
        size(width, itemHeight),
        alignItems.startCenter,
        gap(16.dp),
        paddingLeft(32.dp)
    )

    open val prefix = instructionsOf(
        size(3.dp, itemHeight)
    )

    open fun prefix(active: Boolean) = prefix

    open fun itemColors(active: Boolean, hover: Boolean) =
        colors(active, hover)

}

open class SideBarThemeVariant(
    width : DPixel = 292.dp,
    itemHeight : DPixel = 56.dp
) : SideBarTheme(
    width, itemHeight
) {

    override val item = instructionsOf(
        size(width, itemHeight),
        alignItems.startCenter,
        gap(16.dp)
    )

    override val prefix = instructionsOf(
        height { itemHeight },
        width { 3.dp },
        cornerRadius(0.dp, 10.dp, 0.dp, 10.dp),
        marginRight { 32.dp }
    )

    val activePrefix = prefix + borderRight(colors.primary, 3.dp)

    override fun prefix(active: Boolean) =
        if (active) activePrefix else prefix

    override fun itemColors(active: Boolean, hover: Boolean) =
        when {
            active -> instructionsOf(backgrounds.surface, textColor(colors.primary), svgFill(colors.primary))
            hover -> hoverColors
            else -> normalColors
        }

}