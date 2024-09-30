package `fun`.adaptive.ui.navigation.sidebar

import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.ui.api.box
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
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction


@Adaptive
fun sidebar(
    items: Collection<SideBarItem>,
    navStateOrigin: NavStateOrigin = appNavState,
    theme: SideBarTheme = sideBarTheme,
    vararg instructions: AdaptiveInstruction
): AdaptiveFragment {
    val navState = autoInstance(navStateOrigin)

    column(*instructions, *theme.container) {
        for (item in items.sortedBy { it.index }) {
            sideBarItem(item, navState, theme)
        }
    }

    return fragment()
}

@Adaptive
private fun sideBarItem(
    item: SideBarItem,
    navState: NavState?,
    theme: SideBarTheme = sideBarTheme
): AdaptiveFragment {

    val hover = hover()
    val colors = theme.itemColors(navState in item.state, hover)

    row(*theme.item, *colors) {
        onClick { navState?.goto(item.state) }

        box { theme.prefix(navState in item.state) }
        svg(item.icon) .. theme.icon .. colors
        text(item.title) .. fontSize(16.sp) .. lineHeight(22.dp) .. colors .. noSelect
    }

    return fragment()
}
