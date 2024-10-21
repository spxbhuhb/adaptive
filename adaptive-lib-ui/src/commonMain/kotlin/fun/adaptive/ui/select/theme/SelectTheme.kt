package `fun`.adaptive.ui.select.theme

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.cornerBottomRadius
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.tabIndex
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.input.api.inputTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

open class SelectTheme(
    val itemHeight: Int,
) {

    var outerContainer = instructionsOf(
        height { itemHeight.dp }, // keep it fixed so we won't re-layout it even if the select is open
        tabIndex { 1 }
    )

    var closedContainer = instructionsOf(
        width { inputTheme.width.dp },
        height { itemHeight.dp }
    )

    var base = instructionsOf(
        colTemplate(1.fr, 24.dp),
        height { itemHeight.dp },
        paddingLeft { 16.dp },
        paddingRight(8.dp),
        borders.outline,
        alignItems.startCenter
    )

    var active = base .. textColor(colors.onSurface) .. backgroundColor(colors.surface)

    var focus = active .. border(colors.primary, 2.dp)

    var disabled = base .. backgroundColor(colors.surfaceVariant) .. textColor(colors.onSurfaceVariant)

    var openContainer = instructionsOf(
        width { inputTheme.width.dp },
        zIndex(2),
        backgrounds.surface
    )

    var itemsContainer = instructionsOf(
        position(itemHeight.dp, 0.dp),
        maxWidth,
        verticalScroll,
        border(colors.outline, top = 0.dp),
        cornerBottomRadius(8.dp)
    )

    var item = instructionsOf(
        maxWidth,
        height { itemHeight.dp },
        padding(16.dp)
    )

    fun itemColors(selected: Boolean, hover: Boolean) = colors(false, hover)

}