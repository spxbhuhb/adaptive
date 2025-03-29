package `fun`.adaptive.auth.app

import `fun`.adaptive.runtime.ServerWorkspace

class NoAuthClientModule<WT : ServerWorkspace> : AuthModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        contexts += AuthAppContext()
    }

}