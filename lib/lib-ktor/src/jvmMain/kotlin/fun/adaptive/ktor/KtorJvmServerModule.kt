package `fun`.adaptive.ktor

import `fun`.adaptive.ktor.app.KtorModule
import `fun`.adaptive.ktor.worker.KtorWorker
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.BackendWorkspace

class KtorJvmServerModule<FW : AbstractWorkspace, BW : BackendWorkspace> : KtorModule<FW, BW>() {

    override fun backendWorkspaceInit(workspace: BW, session: Any?) = with(workspace) {
        + KtorWorker()
    }

}