package `fun`.adaptive.value.app

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.runtime.ServerWorkspace
import `fun`.adaptive.value.AvValueDomain
import `fun`.adaptive.value.AvValueServerService
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.persistence.AbstractValuePersistence
import `fun`.adaptive.value.persistence.NoPersistence

class ValueServerModule<WT : ServerWorkspace>(
    val domain : AvValueDomain,
    val authCheck : ServiceImpl<*>.() -> Unit,
    val persistence: AbstractValuePersistence = NoPersistence()
) : ValueModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        AvValueServerService.domain = domain
        AvValueServerService.authCheck = authCheck

        + AvValueServerService()
        + AvValueWorker(domain, persistence)
    }

}