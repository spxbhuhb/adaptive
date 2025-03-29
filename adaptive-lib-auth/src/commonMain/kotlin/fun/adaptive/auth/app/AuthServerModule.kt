package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.backend.*
import `fun`.adaptive.runtime.ServerWorkspace

open class AuthServerModule<WT : ServerWorkspace> : AuthModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        + AuthPrincipalService()
        + AuthRoleService()
        + AuthSessionService()

        + AuthSessionWorker()
        + AuthWorker()
    }

}