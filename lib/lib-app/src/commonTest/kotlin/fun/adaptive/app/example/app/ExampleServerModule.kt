package `fun`.adaptive.app.example.app

import `fun`.adaptive.app.example.server.ExampleService
import `fun`.adaptive.app.example.server.ExampleWorker
import `fun`.adaptive.runtime.BackendWorkspace

class ExampleServerModule<WT : BackendWorkspace> : ExampleModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        // Register server-side services and background workers
        + ExampleService()
        + ExampleWorker()
    }

}