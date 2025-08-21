package `fun`.adaptive.ui.tab

import `fun`.adaptive.ui.menu.MenuItem

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

        val index = tabs.indexOfFirst { it.uuid == pane.uuid }

        val activePane = when {
            ! pane.active -> {
                tabs.firstOrNull { it.active }
            }
            pane.active && index > 0 -> {
                tabs[index - 1]
            }
            tabs.size > 1 -> {
                tabs[1]
            }
            else -> null
        }

        val newTabList = tabs.filter { it.uuid != pane.uuid }.map {
            when {
                it.uuid == activePane?.uuid -> it.copy(active = true)
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

    fun removeOtherTabs(pane: TabPane): TabContainer {
        return TabContainer(
            tabs = listOf(pane.copy(active = true)),
            closeToolTip = closeToolTip,
            menuToolTip = menuToolTip,
            menu = menu,
            switchFun = switchFun,
            closeFun = closeFun
        )
    }

}