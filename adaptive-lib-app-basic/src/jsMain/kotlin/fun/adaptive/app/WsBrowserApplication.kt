package `fun`.adaptive.app

import `fun`.adaptive.app.ws.BasicAppWsModule
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.workspace.Workspace

open class WsBrowserApplication<ADT : UiClientApplicationData>(
    override val appData : ADT,
    vararg modules : AppModule<Workspace>
) : BrowserApplication<Workspace, ADT>() {

    init {
        this.modules += modules
    }

    override val backendMainKey: FragmentKey
        get() = BasicAppWsModule.BACKEND_MAIN_KEY

    override val frontendMainKey: FragmentKey
        get() = BasicAppWsModule.FRONTEND_MAIN_KEY

    override fun initWorkspace() {

        workspace = Workspace(backend)

        super.initWorkspace()

        workspace.addContent(BasicAppWsModule.HOME_CONTENT_ITEM)

        workspace.updateSplits()

    }

}