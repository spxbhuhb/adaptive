package `fun`.adaptive.ktor

import `fun`.adaptive.ktor.app.KtorModule
import `fun`.adaptive.ktor.worker.KtorWorker
import `fun`.adaptive.runtime.BackendWorkspace

class KtorJvmServerModule<WT : BackendWorkspace> : KtorModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with (workspace) {
        + KtorWorker()
    }

}