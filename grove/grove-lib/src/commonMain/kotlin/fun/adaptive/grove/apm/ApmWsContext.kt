package `fun`.adaptive.grove.apm

import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.model.WsContext

class ApmWsContext(
    override val workspace: MultiPaneWorkspace
) : WsContext {

    companion object {
        const val APM_PROJECT_TOOL_KEY = "grove:apm:pane:project"
    }

}