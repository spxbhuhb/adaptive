package `fun`.adaptive.document.app

import `fun`.adaptive.document.backend.DocService
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.ServerWorkspace

open class DocServerModule<WT : ServerWorkspace> : AppModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        + DocService()
    }

}