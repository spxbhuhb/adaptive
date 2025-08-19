package `fun`.adaptive.ui.tab

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuTheme
import `fun`.adaptive.ui.menu.MenuViewBackend
import `fun`.adaptive.ui.menu.menuBackend
import `fun`.adaptive.utility.UUID

data class TabPane(
    val uuid : UUID<TabPane>,
    val key: FragmentKey,
    val title: String? = null,
    val icon: GraphicsResourceSet? = null,
    val tooltip: String? = null,
    val closeable: Boolean = true,
    val menu : MenuViewBackend<*>? = null,
    val actions: List<TabPaneAction> = emptyList(),
    val model: Any? = null,
    val active: Boolean = false
)