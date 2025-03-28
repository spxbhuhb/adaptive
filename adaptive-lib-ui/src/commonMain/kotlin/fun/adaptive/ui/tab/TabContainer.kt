package `fun`.adaptive.ui.tab

import `fun`.adaptive.ui.menu.MenuItem
import kotlin.math.max

class TabContainer(
    val tabs: List<TabPane>,
    val closeToolTip: String? = null,
    val menuToolTip: String? = null,
    val menu: List<MenuItem<*>> = emptyList(),
    val switchFun: (model: TabContainer, tab: TabPane) -> Unit = { _, _ -> },
    val closeFun: (model: TabContainer, tab: TabPane) -> Unit = { _, _ -> }
) {

    fun switchTab(pane: TabPane): TabContainer {

        val newTabList = tabs.map {
            when {
                it.uuid == pane.uuid -> it.copy(active = true)
                it.active -> it.copy(active = false)
                else -> it
            }
        }

        // I don't want a data class here because it messes with equals
        return TabContainer(
            tabs = newTabList,
            closeToolTip = closeToolTip,
            menuToolTip = menuToolTip,
            menu = menu,
            switchFun = switchFun,
            closeFun = closeFun
        )
    }

    fun removeTab(pane: TabPane): TabContainer {

        val activeIndex = if (pane.active) {
            max(0, tabs.indexOfFirst { it.uuid == pane.uuid } - 1)
        } else {
            - 1
        }

        val newTabList = tabs.mapIndexedNotNull { index, it ->
            when {
                it.uuid == pane.uuid -> null
                index != activeIndex -> it
                else -> it.copy(active = true)
            }
        }

        // I don't want a data class here because it messes with equals
        return TabContainer(
            tabs = newTabList,
            closeToolTip = closeToolTip,
            menuToolTip = menuToolTip,
            menu = menu,
            switchFun = switchFun,
            closeFun = closeFun
        )
    }

}