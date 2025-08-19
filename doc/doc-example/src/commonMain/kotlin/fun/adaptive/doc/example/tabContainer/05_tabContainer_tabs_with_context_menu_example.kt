package `fun`.adaptive.doc.example.tabContainer

import `fun`.adaptive.doc.example.generated.resources.menu
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.menuBackend
import `fun`.adaptive.ui.tab.TabContainer
import `fun`.adaptive.ui.tab.TabPane
import `fun`.adaptive.utility.UUID

/**
 * # Tabs with per-tab context menu
 *
 * Each [TabPane] can provide a context menu via its [TabPane.menu] backend. The tab header
 * wraps the handle in [optContextMenu](function://), so right-clicking a tab opens the menu.
 */
@Adaptive
fun tabTabsWithContextMenuExample(): AdaptiveFragment {
    val items = listOf(
        MenuItem(Graphics.menu, "Menu Item 1", "data-1")
    )
    val backend = menuBackend(items) { /* no-op for example */ }

    val model = TabContainer(
        listOf(
            TabPane(
                UUID(),
                "lib:todo",
                "Pane 1",
                tooltip = "Right-click tab for menu",
                menu = backend
            ),
            TabPane(
                UUID(),
                "lib:todo",
                "Pane 2",
                tooltip = "Right-click tab for menu",
                menu = backend
            )
        )
    )

    renderTab(model)
    return fragment()
}
