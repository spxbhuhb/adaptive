package `fun`.adaptive.ui.select

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.cornerBottomRadius
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.tabIndex
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.colorsSurface
import `fun`.adaptive.ui.theme.inputHeightDefault
import `fun`.adaptive.ui.theme.inputWidthDefault

open class SelectTheme(
    val itemHeight : DPixel = inputHeightDefault,
    val itemWidth : DPixel = inputWidthDefault
) {

    var outerContainer = instructionsOf(
        height { itemHeight }, // keep it fixed so we won't re-layout it even if the select is open
        tabIndex { 0 }
    )

    var closedContainer = instructionsOf(
        width { itemWidth },
        height { itemHeight }
    )

    var base = instructionsOf(
        colTemplate(1.fr, 24.dp),
        height { itemHeight },
        alignItems.startCenter
    )

    var enabled = base + instructionsOf(
        borders.outline,
        padding(top = 1.dp, left = 16.dp, bottom = 1.dp, right = 8.dp),
        textColor(colors.onSurface),
        backgroundColor(colors.surface)
    )

    var focused = base + instructionsOf(
        border(colors.primary, 2.dp),
        padding(left = 15.dp, right = 7.dp),
        textColor(colors.onSurface),
        backgroundColor(colors.surface),
    )

    var disabled = base + instructionsOf(
        borders.outline,
        padding(top = 1.dp, left = 16.dp, bottom = 1.dp, right = 16.dp),
        backgroundColor(colors.surfaceVariant),
        textColor(colors.onSurfaceVariant)
    )

    var openContainer = instructionsOf(
        width { itemWidth },
        zIndex(2),
        backgrounds.surface
    )

    var itemsContainer = instructionsOf(
        position(itemHeight, 0.dp),
        maxWidth,
        verticalScroll,
        border(colors.outline, top = 0.dp),
        cornerBottomRadius(8.dp)
    )

    var item = instructionsOf(
        maxWidth,
        height { itemHeight },
        padding(16.dp)
    )

    fun itemColors(selected: Boolean, hover: Boolean) = colorsSurface(false, hover)

    companion object {
        var DEFAULT = SelectTheme()
    }
}