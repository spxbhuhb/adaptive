package `fun`.adaptive.app

import `fun`.adaptive.app.ws.BasicAppWsModule
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.workspace.Workspace
import kotlinx.browser.window

open class WsBrowserClientApplication(
    vararg modules: AppModule<Workspace>
) : BrowserApplication<Workspace>() {

    init {
        this.modules += modules
    }

    override val backendMainKey: FragmentKey
        get() = BasicAppWsModule.BACKEND_MAIN_KEY

    override val frontendMainKey: FragmentKey
        get() = BasicAppWsModule.FRONTEND_MAIN_KEY

    override fun buildWorkspace(session: Any?) {

        workspace = Workspace(backend)

        workspaceInit(workspace, session)

        workspace.updateSplits()

    }

    companion object {
        fun wsBrowserClient(start: Boolean = true, buildFun: WsBrowserClientBuilder.() -> Unit) {
            val builder = WsBrowserClientBuilder()

            builder.buildFun()

            if (start) {
                WsBrowserClientApplication(*builder.modules.toTypedArray()).main()
            }
        }
    }
}