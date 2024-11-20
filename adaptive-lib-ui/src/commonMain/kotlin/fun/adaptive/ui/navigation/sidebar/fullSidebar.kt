package `fun`.adaptive.ui.navigation.sidebar

import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.hover
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.NavStateOrigin
import `fun`.adaptive.ui.navigation.appNavState
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.navigation.sidebar.theme.FullSidebarTheme
import `fun`.adaptive.ui.navigation.sidebar.theme.fullSidebarTheme


@Adaptive
fun fullSidebar(
    items: Collection<SidebarItem>,
    navStateOrigin: NavStateOrigin = appNavState,
    theme: FullSidebarTheme = fullSidebarTheme,
    vararg instructions: AdaptiveInstruction
): AdaptiveFragment {
    val navState = autoItemOrigin(navStateOrigin)

    column(*instructions, *theme.container) {
        for (item in items.sortedBy { it.index }) {
            fullSidebarItem(item, navState, theme)
        }
    }

    return fragment()
}

@Adaptive
private fun fullSidebarItem(
    item: SidebarItem,
    navState: NavState?,
    theme: FullSidebarTheme
): AdaptiveFragment {

    val hover = hover()
    val colors = theme.itemColors(navState in item.state, hover)

    row(*theme.item, *colors) {
        onClick { navState?.goto(item.state) }

        box { theme.prefix(navState in item.state) }
        svg(item.icon) .. theme.icon .. colors
        text(item.title) .. theme.label .. colors
    }

    return fragment()
}
