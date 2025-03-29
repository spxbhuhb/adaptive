package `fun`.adaptive.auth.app

import `fun`.adaptive.runtime.ClientWorkspace

class NoAuthClientModule<WT : ClientWorkspace> : AuthModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        contexts += AuthAppContext(workspace)
    }

}