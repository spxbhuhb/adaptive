package `fun`.adaptive.ui.tab

import `fun`.adaptive.ui.menu.MenuItem

class TabContainer(
    val tabs: List<TabPane>,
    val closeToolTip : String? = null,
    val menuToolTip: String? = null,
    val menu: List<MenuItem> = emptyList()
)