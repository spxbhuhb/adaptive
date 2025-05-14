package `fun`.adaptive.app

import `fun`.adaptive.app.builder.ApplicationBuilder
import `fun`.adaptive.app.ws.wsAppMain
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace

open class WsBrowserClientApplication(
    vararg modules: AppModule<MultiPaneWorkspace>
) : BrowserApplication<MultiPaneWorkspace>() {

    init {
        this.modules += modules
    }

    override val backendMainKey: FragmentKey
        get() = wsAppMain.BACKEND_MAIN_KEY

    override val frontendMainKey: FragmentKey
        get() = wsAppMain.FRONTEND_MAIN_KEY

    override fun buildWorkspace() {

        workspace = MultiPaneWorkspace(backend)
        workspace.applicationOrNull = this

        workspaceInit(workspace)

        workspace.updateSplits()

    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) {
        workspace.frontendOrNull = adapter
        super.frontendAdapterInit(adapter)
    }


    companion object {
        fun wsBrowserClient(start: Boolean = true, buildFun: ApplicationBuilder<MultiPaneWorkspace>.() -> Unit) {
            val builder = ApplicationBuilder<MultiPaneWorkspace>()

            builder.buildFun()

            if (start) {
                WsBrowserClientApplication(*builder.modules.toTypedArray()).main()
            }
        }
    }
}