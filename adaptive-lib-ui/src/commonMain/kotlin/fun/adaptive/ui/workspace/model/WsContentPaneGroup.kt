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
    firstPane : WsPane<*>
) {
    /**
     * True when this group contains only one pane and that one pane
     * cannot be changed. Singular pane groups does not show tabs.
     */
    var singular : Boolean = false

    /**
     * The active pane of this pane group. This is the pane that is currently
     * shown.
     */
    val activePane = storeFor<WsPane<*>> { firstPane }

    val panes = storeFor<List<WsPane<*>>> { listOf(firstPane) }

    fun toTabContainer() : TabContainer =
        TabContainer(
            panes.value.map { wsPane ->
                TabPane(
                    wsPane.uuid.cast(),
                    wsPane.key,
                    wsPane.name,
                    wsPane.icon,
                    wsPane.tooltip,
                    closeable = true,
                    wsPane.actions.map { wsAction ->
                        TabPaneAction(
                            wsAction.icon,
                            wsAction.tooltip,
                            { wsAction.action(workspace, wsPane) }
                        )
                    },
                    model = wsPane
                )
            }
        )

}