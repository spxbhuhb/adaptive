package `fun`.adaptive.ui.mpw.backends

import `fun`.adaptive.foundation.value.observableOf
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuViewBackend
import `fun`.adaptive.ui.menu.menuBackend
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.generated.resources.closeAllTabs
import `fun`.adaptive.ui.mpw.generated.resources.closeOtherTabs
import `fun`.adaptive.ui.mpw.generated.resources.closeTab
import `fun`.adaptive.ui.mpw.model.PaneAction
import `fun`.adaptive.ui.tab.TabContainer
import `fun`.adaptive.ui.tab.TabPane
import `fun`.adaptive.ui.tab.TabPaneAction
import `fun`.adaptive.utility.UUID

class ContentPaneGroupViewBackend(
    val uuid: UUID<ContentPaneGroupViewBackend>,
    val workspace: MultiPaneWorkspace,
    firstPane: PaneViewBackend<*>
) {
    /**
     * True when this group contains only one pane and that one pane
     * cannot be changed. Singular pane groups do not show tabs.
     */
    val isSingular: Boolean
        get() = (panes.size == 1 && panes.first().paneDef.singularity.isSingular)

    /**
     * The active pane of this pane group. This is the pane that is currently
     * shown.
     */
    var activePane = firstPane
        private set

    var panes = mutableListOf(firstPane)
        private set

    val tabContainer = observableOf { toTabContainer() }

    fun load(pane: PaneViewBackend<*>) {
        val index = panes.indexOfFirst { it.uuid == pane.uuid }

        if (index == - 1) {
            panes += pane
        } else {
            panes[index] = pane
        }

        activePane = pane
        tabContainer.value = toTabContainer()
    }

    fun toTabContainer(): TabContainer =
        TabContainer(
            panes.map { pane ->
                TabPane(
                    pane.uuid.cast(),
                    pane.paneDef.fragmentKey,
                    pane.name ?: pane.paneDef.name,
                    pane.icon ?: pane.paneDef.icon,
                    pane.tooltip ?: pane.paneDef.tooltip,
                    closeable = true,
                    menu = paneMenu(pane),
                    actions =pane.getPaneActions().mapNotNull { wsAction ->
                        // TODO context menu actions for content panes
                        if (wsAction !is PaneAction) return@mapNotNull null
                        TabPaneAction(
                            wsAction.icon,
                            wsAction.tooltip
                        ) { wsAction.execute() }
                    },
                    model = pane,
                    active = (pane.uuid == activePane.uuid)
                )
            },
            switchFun = ::switchTab,
            closeFun = ::closeTab
        )

    fun paneMenu(pane : PaneViewBackend<*>) : MenuViewBackend<*> {
        return menuBackend(
            listOf(
                MenuItem(null, Strings.closeTab, pane.uuid),
                MenuItem(null, Strings.closeOtherTabs, pane.uuid),
                MenuItem(null, Strings.closeAllTabs, pane.uuid)
            )
        ) { menuEvent ->
            when (menuEvent.item.label) {
                Strings.closeTab -> {
                    val tab = tabContainer.value.tabs.firstOrNull { it.uuid == menuEvent.item.data }
                    if (tab != null) {
                        closeTab(tabContainer.value, tab)
                    }
                }
                Strings.closeOtherTabs -> {
                    val tab = tabContainer.value.tabs.firstOrNull { it.uuid == menuEvent.item.data }
                    if (tab != null) {
                        closeOtherTabs(tabContainer.value, tab)
                    }
                }
                Strings.closeAllTabs -> {
                    workspace.removePaneGroup(this)
                }
            }
            menuEvent.closeMenu()
        }
    }

    fun switchTab(model : TabContainer, pane : TabPane) {
        val newTabs = model.switchTab(pane)
        activePane = newTabs.tabs.first { it.active }.model as PaneViewBackend<*>
        tabContainer.value = newTabs
    }

    fun closeOtherTabs(model : TabContainer, pane : TabPane) {
        panes.removeAll { it.uuid != pane.uuid }

        if (panes.isEmpty()) {
            workspace.removePaneGroup(this)
        } else {
            val newTabs = model.removeOtherTabs(pane)
            activePane = newTabs.tabs.first { it.active }.model as PaneViewBackend<*>
            tabContainer.value = newTabs
        }
    }

    fun closeTab(model : TabContainer, pane : TabPane) {
        panes.removeAll { it.uuid == pane.uuid }

        if (panes.isEmpty()) {
            workspace.removePaneGroup(this)
        } else {
            val newTabs = model.removeTab(pane)
            activePane = newTabs.tabs.first { it.active }.model as PaneViewBackend<*>
            tabContainer.value = newTabs
        }
    }

}