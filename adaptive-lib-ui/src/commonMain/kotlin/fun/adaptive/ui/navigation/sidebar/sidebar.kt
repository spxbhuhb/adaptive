package `fun`.adaptive.ui.navigation.sidebar

import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.hover
import `fun`.adaptive.ui.api.lineHeight
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.NavStateOrigin
import `fun`.adaptive.ui.navigation.appNavState

@Adaptive
fun sidebar(items : Collection<SideBarItem>, navStateOrigin : NavStateOrigin = appNavState) {
    val navState = autoInstance(navStateOrigin)

    column {
        for (item in items.sortedBy { it.index }) {
            sideBarItem(item, navState)
        }
    }
}

@Adaptive
private fun sideBarItem(
    item: SideBarItem,
    navState: NavState?
) : AdaptiveFragment {

    val hover = hover()
    val colors = sideBarTheme.itemColors(navState in item.state, hover)

    row(*sideBarTheme.item, *colors) {
        onClick { navState?.goto(item.state) }

        svg(item.icon) .. sideBarTheme.icon .. colors
        text(item.title) .. fontSize(16.sp) .. lineHeight(22.dp) .. colors .. noSelect
    }

    return fragment()
}
