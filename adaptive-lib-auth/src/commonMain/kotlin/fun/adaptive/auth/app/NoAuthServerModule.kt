package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.backend.AuthNullSessionService
import `fun`.adaptive.auth.backend.AuthSessionWorker
import `fun`.adaptive.runtime.ServerWorkspace

class NoAuthServerModule<WT : ServerWorkspace> : AuthModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        + AuthNullSessionService()
        + AuthSessionWorker()
    }

}