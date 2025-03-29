package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.backend.basic.AuthBasicService
import `fun`.adaptive.runtime.ServerWorkspace

class AuthBasicServerModule<WT : ServerWorkspace> : AuthServerModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) {
        super.workspaceInit(workspace, session)

        with(workspace) {
            + AuthBasicService()
        }
    }

}