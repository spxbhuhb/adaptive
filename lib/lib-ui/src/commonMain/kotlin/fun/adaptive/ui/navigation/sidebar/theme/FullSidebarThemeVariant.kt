package `fun`.adaptive.ui.navigation.sidebar.theme

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.borderRight
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.marginRight
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.hoverColors
import `fun`.adaptive.ui.theme.normalColors


open class FullSidebarThemeVariant(
    width : DPixel = 292.dp,
    itemHeight : DPixel = 56.dp
) : FullSidebarTheme(
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
            active -> instructionsOf(backgrounds.surface, textColor(colors.primary), fill(colors.primary))
            hover -> hoverColors
            else -> normalColors
        }

}