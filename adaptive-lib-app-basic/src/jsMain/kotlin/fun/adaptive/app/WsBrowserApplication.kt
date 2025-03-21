package `fun`.adaptive.app

import `fun`.adaptive.app.ws.WsAppModule
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.workspace.Workspace

open class WsBrowserApplication(
    vararg modules : AppModule<Workspace>
) : BrowserApplication<Workspace>() {

    init {
        this.modules += modules
    }

    override val backendMainKey: FragmentKey
        get() = WsAppModule.BACKEND_MAIN_KEY

    override val frontendMainKey: FragmentKey
        get() = WsAppModule.FRONTEND_MAIN_KEY

    override fun initWorkspace() {

        workspace = Workspace(backend)

        super.initWorkspace()

        workspace.addContent(WsAppModule.HOME_CONTENT_ITEM)

        workspace.updateSplits()

    }

}