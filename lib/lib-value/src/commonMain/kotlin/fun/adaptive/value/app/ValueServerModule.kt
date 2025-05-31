package `fun`.adaptive.value.app

import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.value.AvValueServerService
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.persistence.AbstractValuePersistence
import `fun`.adaptive.value.persistence.NoPersistence

class ValueServerModule<FW : AbstractWorkspace, BW : BackendWorkspace>(
    val persistence: AbstractValuePersistence = NoPersistence()
) : ValueModule<FW,BW>() {

    override fun backendWorkspaceInit(workspace: BW, session: Any?) = with(workspace) {
        + AvValueServerService()
        + AvValueWorker(proxy = false, persistence)
    }

}