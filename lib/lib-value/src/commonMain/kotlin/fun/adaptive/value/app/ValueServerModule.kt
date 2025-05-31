package `fun`.adaptive.value.app

import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.value.AvValueServerService
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.persistence.AbstractValuePersistence
import `fun`.adaptive.value.persistence.NoPersistence

class ValueServerModule<WT : BackendWorkspace>(
    val persistence: AbstractValuePersistence = NoPersistence()
) : ValueModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        + AvValueServerService()
        + AvValueWorker(proxy = false, persistence)
    }

}