package `fun`.adaptive.value.app

import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.value.AvValueClientService
import `fun`.adaptive.value.AvValueWorker

class ValueClientModule<FW : AbstractWorkspace, BW : BackendWorkspace> : ValueModule<FW, BW>() {

    override fun backendWorkspaceInit(workspace: BW, session: Any?) {
        super.backendWorkspaceInit(workspace, session)

        with (workspace) {
            + AvValueClientService()
            + AvValueWorker(proxy = true)
        }
    }
}