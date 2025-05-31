package `fun`.adaptive.sandbox.app.echo.app

import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.sandbox.app.echo.server.EchoService

class EchoServerModule<WT : BackendWorkspace> : AppModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        + EchoService()
    }

}