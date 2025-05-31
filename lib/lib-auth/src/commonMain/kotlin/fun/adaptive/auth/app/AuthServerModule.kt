package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.backend.*
import `fun`.adaptive.runtime.BackendWorkspace

open class AuthServerModule<WT : BackendWorkspace> : AuthModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        + AuthPrincipalService()
        + AuthRoleService()
        + AuthSessionService()

        + AuthSessionWorker()
        + AuthWorker()
    }

}