package `fun`.adaptive.auth.app

import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.BackendWorkspace

class NoAuthClientModule<FW : AbstractWorkspace, BW : BackendWorkspace> : AuthModule<FW, BW>() {

    override fun frontendWorkspaceInit(workspace: FW, session: Any?) = with(workspace) {
        // FIXME clean up auth context vs. workspace (backend or frontend)
        contexts += AuthAppContext(workspace)
    }

}