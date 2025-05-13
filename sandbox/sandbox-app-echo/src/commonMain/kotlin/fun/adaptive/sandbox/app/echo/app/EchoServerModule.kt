package `fun`.adaptive.sandbox.app.echo.app

import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.ServerWorkspace
import `fun`.adaptive.sandbox.app.echo.server.EchoService

class EchoServerModule<WT : ServerWorkspace> : AppModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        + EchoService()
    }

}