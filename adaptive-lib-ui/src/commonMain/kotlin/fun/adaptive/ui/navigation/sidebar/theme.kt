package `fun`.adaptive.ui.navigation.sidebar

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.colors

var sideBarTheme = SideBarTheme()

open class SideBarTheme {

    open var icon = instructionsOf(
        size(24.dp, 24.dp)
    )

    open var item = instructionsOf(
        size(314.dp, 63.dp),
        alignItems.startCenter,
        gap(16.dp),
        paddingLeft(32.dp)
    )

    open fun itemColors(active: Boolean, hover : Boolean) =
        colors(active, hover)

}