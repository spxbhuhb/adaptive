package `fun`.adaptive.ui.tab

import `fun`.adaptive.ui.menu.MenuItem
import kotlin.math.max

data class TabContainer(
    val tabs: List<TabPane>,
    val closeToolTip: String? = null,
    val menuToolTip: String? = null,
    val menu: List<MenuItem<*>> = emptyList(),
    val closeFun: (model: TabContainer, tab: TabPane) -> Unit = { _, _ -> }
) {

    fun removePane(pane: TabPane): TabContainer {

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

        return copy(tabs = newTabList)
    }

}