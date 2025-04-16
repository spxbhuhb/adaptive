package `fun`.adaptive.app

import `fun`.adaptive.app.ws.wsAppMain
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.workspace.Workspace

open class WsBrowserClientApplication(
    vararg modules: AppModule<Workspace>
) : BrowserApplication<Workspace>() {

    init {
        this.modules += modules
    }

    override val backendMainKey: FragmentKey
        get() = wsAppMain.BACKEND_MAIN_KEY

    override val frontendMainKey: FragmentKey
        get() = wsAppMain.FRONTEND_MAIN_KEY

    override fun buildWorkspace() {

        workspace = Workspace(backend)
        workspace.applicationOrNull = this

        workspaceInit(workspace)

        workspace.updateSplits()

    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) {
        workspace.frontendOrNull = adapter
        super.frontendAdapterInit(adapter)
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