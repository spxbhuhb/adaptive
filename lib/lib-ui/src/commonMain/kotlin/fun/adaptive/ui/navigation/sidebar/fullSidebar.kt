package `fun`.adaptive.ui.navigation.sidebar

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.general.Observable
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.sidebar.theme.FullSidebarTheme
import `fun`.adaptive.ui.navigation.sidebar.theme.fullSidebarTheme


@Adaptive
fun fullSidebar(
    items: Collection<SidebarItem>,
    navStateOrigin: Observable<NavState>,
    theme: FullSidebarTheme = fullSidebarTheme,
    vararg instructions: AdaptiveInstruction,
): AdaptiveFragment {

    val navState = observe { navStateOrigin }

    column(theme.container, instructions()) {
        for (item in items.sortedBy { it.index }) {
            fullSidebarItem(item, navState, theme)
        }
    }

    return fragment()
}

@Adaptive
fun fullSidebarItem(
    item: SidebarItem,
    navState: NavState?,
    theme: FullSidebarTheme
): AdaptiveFragment {

    val hover = hover()
    val colors = theme.itemColors(navState in item.state, hover)

    row(theme.item, colors) {
        onClick { navState?.goto(item.state) }

        box { theme.prefix(navState in item.state) }
        svg(item.icon) .. theme.icon .. colors
        text(item.title) .. theme.label .. colors
    }

    return fragment()
}
