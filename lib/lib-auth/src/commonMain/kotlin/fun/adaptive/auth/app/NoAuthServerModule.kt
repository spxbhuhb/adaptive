package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.backend.AuthNullSessionService
import `fun`.adaptive.auth.backend.AuthSessionWorker
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.BackendWorkspace

class NoAuthServerModule<FW : AbstractWorkspace, BW : BackendWorkspace> : AuthModule<FW, BW>() {

    override fun backendWorkspaceInit(workspace: BW, session: Any?) = with(workspace) {
        + AuthNullSessionService()
        + AuthSessionWorker()
    }

}