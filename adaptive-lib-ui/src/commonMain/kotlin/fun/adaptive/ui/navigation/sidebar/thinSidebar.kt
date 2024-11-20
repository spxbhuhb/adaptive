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
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.NavStateOrigin
import `fun`.adaptive.ui.navigation.appNavState
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.navigation.sidebar.theme.ThinSidebarTheme
import `fun`.adaptive.ui.navigation.sidebar.theme.thinSidebarTheme


@Adaptive
fun thinSidebar(
    items: Collection<SidebarItem>,
    navStateOrigin: NavStateOrigin = appNavState,
    theme: ThinSidebarTheme = thinSidebarTheme,
    vararg instructions: AdaptiveInstruction
): AdaptiveFragment {
    val navState = autoItemOrigin(navStateOrigin)

    column(*instructions, *theme.container) {
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

    column(*theme.item) {
        onClick { navState?.goto(item.state) }

        box(*theme.iconContainer, *icon) {
            svg(item.icon) .. theme.icon .. icon
        }

        text(item.title) .. label
    }

    return fragment()
}
