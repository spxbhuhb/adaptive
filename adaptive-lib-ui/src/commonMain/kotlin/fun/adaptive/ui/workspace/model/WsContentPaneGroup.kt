package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.ui.tab.TabContainer
import `fun`.adaptive.ui.tab.TabPane
import `fun`.adaptive.ui.tab.TabPaneAction
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.utility.UUID

class WsContentPaneGroup(
    val uuid : UUID<WsContentPaneGroup>,
    val workspace: Workspace,
    firstPane: WsPane<*, *>
) {
    /**
     * True when this group contains only one pane and that one pane
     * cannot be changed. Singular pane groups does not show tabs.
     */
    val isSingular : Boolean
        get() = (panes.size == 1 && panes.first().singularity == WsPaneSingularity.SINGULAR)

    /**
     * The active pane of this pane group. This is the pane that is currently
     * shown.
     */
    var activePane = firstPane
        private set

    var panes = mutableListOf(firstPane)
        private set

    val tabContainer = storeFor<TabContainer> { toTabContainer() }

    fun load(pane: WsPane<*, *>) {
        val index = panes.indexOfFirst { it.uuid == pane.uuid }

        if (index == -1) {
            panes += pane
        } else {
            panes[index] = pane
        }

        activePane = pane
        tabContainer.value = toTabContainer()
    }

    fun toTabContainer() : TabContainer =
        TabContainer(
            panes.map { wsPane ->
                TabPane(
                    wsPane.uuid.cast(),
                    wsPane.key,
                    wsPane.name,
                    wsPane.icon,
                    wsPane.tooltip,
                    closeable = true,
                    wsPane.actions.mapNotNull { wsAction ->
                        // TODO context menu actions for content panes
                        if (wsAction !is WsPaneAction<*>) return@mapNotNull null
                        TabPaneAction(
                            wsAction.icon,
                            wsAction.tooltip,
                            { wsAction.execute(workspace, wsPane) }
                        )
                    },
                    model = wsPane,
                    active = (wsPane.uuid == activePane.uuid)
                )
            }
        )

}