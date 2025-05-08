package my.project.example.app

import my.project.example.server.ExampleService
import `fun`.adaptive.runtime.ServerWorkspace

class ExampleServerModule<WT : ServerWorkspace> : ExampleModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        + ExampleService()
    }
}
