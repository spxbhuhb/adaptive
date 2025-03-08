package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.ui.tab.TabContainer
import `fun`.adaptive.ui.tab.TabPane
import `fun`.adaptive.ui.tab.TabPaneAction

class WorkspaceContentPaneGroup(
    val workspace: Workspace
) {
    val activePane = storeFor<WorkspacePane?> { null }

    val panes = storeFor<List<WorkspacePane>> { emptyList() }

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
                    }
                )
            }
        )

}