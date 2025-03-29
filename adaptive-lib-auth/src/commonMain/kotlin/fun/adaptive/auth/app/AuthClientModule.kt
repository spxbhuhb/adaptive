package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.runtime.ClientWorkspace

class AuthClientModule<WT : ClientWorkspace> : AuthModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        contexts += AuthAppContext(workspace).also {
            if (session != null) {
                check(session is Session)
                it.sessionOrNull = session
            }
        }
    }

}