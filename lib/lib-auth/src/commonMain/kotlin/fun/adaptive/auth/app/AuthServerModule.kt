package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.backend.*
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.BackendWorkspace

open class AuthServerModule<FW : AbstractWorkspace, BW: BackendWorkspace> : AuthModule<FW,BW>() {

    override fun backendWorkspaceInit(workspace: BW, session: Any?) = with(workspace) {
        + AuthPrincipalService()
        + AuthRoleService()
        + AuthSessionService()

        + AuthSessionWorker()
        + AuthWorker()
    }

}