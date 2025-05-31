package `fun`.adaptive.app.example.app

import `fun`.adaptive.app.example.server.ExampleService
import `fun`.adaptive.app.example.server.ExampleWorker
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.BackendWorkspace

class ExampleServerModule<FW : AbstractWorkspace, BW : BackendWorkspace> : ExampleModule<FW,BW>() {

    override fun backendWorkspaceInit(workspace: BW, session: Any?) = with(workspace) {
        // Register server-side services and background workers
        + ExampleService()
        + ExampleWorker()
    }

}