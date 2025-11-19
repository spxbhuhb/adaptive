package `fun`.adaptive.ui.navigation.sidebar

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.general.Observable
import `fun`.adaptive.graphics.svg.api.svgOrImage
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.sidebar.theme.ThinSidebarTheme
import `fun`.adaptive.ui.navigation.sidebar.theme.thinSidebarTheme


@Adaptive
fun thinSidebar(
    items: Collection<SidebarItem>,
    navStateOrigin: Observable<NavState>,
    theme: ThinSidebarTheme = thinSidebarTheme,
    vararg instructions: AdaptiveInstruction,
): AdaptiveFragment {
    val navState = observe { navStateOrigin }

    column(theme.container, instructions()) {
        for (item in items.sortedBy { it.index }) {
            thinSidebarItem(item, navState, theme)
        }
    }

    return fragment()
}

@Adaptive
private fun thinSidebarItem(
    item: SidebarItem,
    navState: NavState?,
    theme: ThinSidebarTheme
): AdaptiveFragment {

    val hover = hover()

    val icon = theme.icon(navState in item.state, hover)
    val label = theme.label(navState in item.state, hover)

    column(theme.item) {
        onClick { navState?.goto(item.state) }

        box(theme.iconContainer, icon) {
            svgOrImage(item.icon) .. theme.icon .. icon
        }

        text(item.title) .. label
    }

    return fragment()
}
