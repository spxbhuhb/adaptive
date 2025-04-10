package `fun`.adaptive.cookbook.app.echo

import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.ServerWorkspace

class EchoServerModule<WT : ServerWorkspace> : AppModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with (workspace) {
        + EchoService()
    }

}