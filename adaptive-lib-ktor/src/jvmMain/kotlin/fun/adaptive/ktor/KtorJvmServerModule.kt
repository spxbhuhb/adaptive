package `fun`.adaptive.ktor

import `fun`.adaptive.auth.model.AccessDenied
import `fun`.adaptive.ktor.worker.KtorWorker
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.ServerWorkspace
import `fun`.adaptive.wireformat.WireFormatRegistry

class KtorJvmServerModule<WT : ServerWorkspace> : AppModule<WT>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with (registry) {
        + AccessDenied
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with (workspace) {
        + KtorWorker()
    }

}