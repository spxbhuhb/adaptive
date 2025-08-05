package `fun`.adaptive.app

import `fun`.adaptive.app.builder.ApplicationBuilder
import `fun`.adaptive.app.ui.mpw.mpwAppMainModule
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.runtime.AppAboutData
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.utility.Url

open class MpwBrowserClientApplication(
    override val about: AppAboutData,
    vararg modules: AppModule<MultiPaneWorkspace, BackendWorkspace>
) : BrowserApplication<MultiPaneWorkspace>() {

    init {
        this.modules += modules
    }

    override val backendMainKey: FragmentKey
        get() = mpwAppMainModule.BACKEND_MAIN_KEY

    override val frontendMainKey: FragmentKey
        get() = mpwAppMainModule.FRONTEND_MAIN_KEY

    override fun buildFrontendWorkspace() {

        frontendWorkspace = MultiPaneWorkspace(backend, backendWorkspace)

        frontendWorkspaceInit(frontendWorkspace)

        frontendWorkspace.loadUrl(navState.value.url)

        frontendWorkspace.updateSplits()

    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) {
        frontendWorkspace.frontendOrNull = adapter
        super.frontendAdapterInit(adapter)
    }

    override fun setNavState(url: Url) {
        historyStateListener.push(NavState(url))
    }

    override fun onNavStateChange(callback: (Url) -> Unit) {
        navState.addListener { callback(it.url) } // FIXME clean up NavState / URL / application
    }

    companion object {
        fun wsBrowserClient(
            about : AppAboutData = AppAboutData(),
            start: Boolean = true,
            buildFun: ApplicationBuilder<MultiPaneWorkspace, BackendWorkspace>.() -> Unit
        ) {
            val builder = ApplicationBuilder<MultiPaneWorkspace, BackendWorkspace>()

            builder.buildFun()

            if (start) {
                MpwBrowserClientApplication(about, *builder.modules.toTypedArray()).main()
            }
        }
    }
}