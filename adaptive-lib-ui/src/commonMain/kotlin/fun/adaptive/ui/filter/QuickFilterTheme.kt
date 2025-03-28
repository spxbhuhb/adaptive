package `fun`.adaptive.ui.filter

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.AbstractTheme
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

open class FilterTheme : AbstractTheme() {

    companion object {
        var DEFAULT = FilterTheme()
    }

    /**
     * Outer height of the filter row.
     */
    open var height = height(inputHeightDp)

    /**
     * Inner height of the filter row. This is the outer
     * height minus borders on top and bottom. The active item
     * overlaps the borders, so we need to handle the difference.
     */
    open var innerHeight = height(36.dp)

    open var container = instructionsOf(
        alignItems.center,
        gap(6.dp),
        borders.outline,
        inputCornerRadius,
        backgrounds.surface,
        height { inputHeightDp }
    )

    open var label = instructionsOf(
        buttonFont,
        noSelect
    )

    var item = instructionsOf(
        padding(9.dp, 12.dp, 7.dp, 12.dp),
        alignItems.center,
        inputCornerRadius
    )

    open fun item(active: Boolean, hover : Boolean) =
       item + colors(active, hover) + if (active || hover) height else innerHeight

}