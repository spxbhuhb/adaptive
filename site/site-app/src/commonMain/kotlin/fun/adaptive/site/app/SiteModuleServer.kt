package `fun`.adaptive.site.app

import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.site.server.SiteWorker

class SiteModuleServer<FW : AbstractWorkspace,BW : BackendWorkspace> : AppModule<FW, BW>() {

    override fun backendWorkspaceInit(workspace: BW, session: Any?) {
        with (workspace) {
            + SiteWorker()
        }
    }

}