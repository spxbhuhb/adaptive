package `fun`.adaptive.grove.apm

import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsContext

class ApmWsContext(
    override val workspace: Workspace
) : WsContext {

    companion object {
        const val APM_PROJECT_TOOL_KEY = "grove:apm:pane:project"
    }

}