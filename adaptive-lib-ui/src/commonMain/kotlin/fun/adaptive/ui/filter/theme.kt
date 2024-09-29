package `fun`.adaptive.ui.filter

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.lightFont
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

var filterTheme = FilterTheme()

open class FilterTheme {

    /**
     * Outer height of the filter row.
     */
    open var height = height(38.dp)

    /**
     * Inner height of the filter row. This is the outer
     * height minus borders on top and bottom. The active item
     * overlaps the borders, so we need to handle the difference.
     */
    open var innerHeight = height(36.dp)

    open var container = instructionsOf(
        alignItems.center,
        gap(8.dp),
        borders.outline,
        cornerRadius(10.dp),
        backgrounds.surface,
        height
    )

    open var label = instructionsOf(
        fontSize(14.sp),
        lightFont,
        noSelect
    )

    var item = instructionsOf(
        padding(8.dp, 16.dp, 8.dp, 16.dp),
        alignItems.center,
        cornerRadius(10.dp)
    )

    open fun item(active: Boolean, hover : Boolean) =
       item + colors(active, hover) + if (active || hover) height else innerHeight

}