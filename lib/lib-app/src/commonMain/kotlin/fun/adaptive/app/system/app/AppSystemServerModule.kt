package `fun`.adaptive.app.system.app

import `fun`.adaptive.runtime.ServerWorkspace

class AppSystemServerModule<WT : ServerWorkspace> : AppSystemModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        // add worker and service registrations here
    }

}