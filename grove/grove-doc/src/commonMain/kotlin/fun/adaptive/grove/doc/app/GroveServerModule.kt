package `fun`.adaptive.grove.doc.app

import `fun`.adaptive.grove.doc.server.GroveDocService
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.BackendWorkspace

class GroveServerModule : AppModule<AbstractWorkspace, BackendWorkspace>() {

    override fun backendWorkspaceInit(workspace: BackendWorkspace, session: Any?) = with(workspace) {
        + GroveDocService()
    }
}