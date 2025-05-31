package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.runtime.FrontendWorkspace

open class AuthClientModule<FW : AbstractWorkspace, BW: BackendWorkspace> : AuthModule<FW,BW>() {

    override fun frontendWorkspaceInit(workspace: FW, session: Any?) = with(workspace) {
        contexts += AuthAppContext(workspace).also {
            if (session != null) {
                check(session is Session)
                it.sessionOrNull = session
            }
        }
    }

}