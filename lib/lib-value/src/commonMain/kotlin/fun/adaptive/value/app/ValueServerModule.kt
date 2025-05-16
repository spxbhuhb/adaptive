package `fun`.adaptive.value.app

import `fun`.adaptive.runtime.ServerWorkspace
import `fun`.adaptive.value.AvValueDomain
import `fun`.adaptive.value.AvValueServerService
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.persistence.AbstractValuePersistence
import `fun`.adaptive.value.persistence.NoPersistence

class ValueServerModule<WT : ServerWorkspace>(
    val domain : AvValueDomain,
    val persistence: AbstractValuePersistence = NoPersistence()
) : ValueModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        + AvValueServerService()
        + AvValueWorker(domain, proxy = false, persistence)
    }

}