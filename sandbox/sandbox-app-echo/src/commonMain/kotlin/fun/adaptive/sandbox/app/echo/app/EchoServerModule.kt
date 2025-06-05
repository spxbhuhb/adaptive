package `fun`.adaptive.sandbox.app.echo.app

import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.sandbox.app.echo.server.EchoService

class EchoServerModule<FW : AbstractWorkspace, BW : BackendWorkspace> : AppModule<FW,BW>() {

    override fun backendWorkspaceInit(workspace: BW, session: Any?) = with(workspace) {
        + EchoService()
    }

}