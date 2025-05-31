package `fun`.adaptive.app

import `fun`.adaptive.app.builder.ApplicationBuilder
import `fun`.adaptive.app.ws.wsAppMain
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace

open class WsBrowserClientApplication(
    vararg modules: AppModule<MultiPaneWorkspace, BackendWorkspace>
) : BrowserApplication<MultiPaneWorkspace>() {

    init {
        this.modules += modules
    }

    override val backendMainKey: FragmentKey
        get() = wsAppMain.BACKEND_MAIN_KEY

    override val frontendMainKey: FragmentKey
        get() = wsAppMain.FRONTEND_MAIN_KEY

    override fun buildFrontendWorkspace() {

        frontendWorkspace = MultiPaneWorkspace(backend, backendWorkspace)
        frontendWorkspace.applicationOrNull = this

        frontendWorkspaceInit(frontendWorkspace)

        frontendWorkspace.updateSplits()

    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) {
        frontendWorkspace.frontendOrNull = adapter
        super.frontendAdapterInit(adapter)
    }


    companion object {
        fun wsBrowserClient(start: Boolean = true, buildFun: ApplicationBuilder<MultiPaneWorkspace, BackendWorkspace>.() -> Unit) {
            val builder = ApplicationBuilder<MultiPaneWorkspace, BackendWorkspace>()

            builder.buildFun()

            if (start) {
                WsBrowserClientApplication(*builder.modules.toTypedArray()).main()
            }
        }
    }
}