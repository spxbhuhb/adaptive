package `fun`.adaptive.doc.example.tabContainer

import `fun`.adaptive.doc.example.generated.resources.menu
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.tab.TabContainer
import `fun`.adaptive.ui.tab.tabContainer
import `fun`.adaptive.ui.tab.tabHandle

/**
 * # No tabs, container menu
 *
 * Demonstrates a container-level menu without any tabs. The menu button appears
 * in the header when [menu](property://TabContainer) is not empty.
 */
@Adaptive
fun tabNoTabsWithMenuExample(): AdaptiveFragment {

    val backend = TabContainer(
        emptyList(),
        closeToolTip = "Close tab",
        menuToolTip = "Tab Container Menu",
        menu = listOf(MenuItem(Graphics.menu, "Menu Item 1", Unit))
    )

    tabContainer(backend, ::tabHandle)

    return fragment()
}
