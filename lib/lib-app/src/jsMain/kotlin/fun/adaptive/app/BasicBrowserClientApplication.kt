package `fun`.adaptive.app

import `fun`.adaptive.app.builder.ApplicationBuilder
import `fun`.adaptive.app.app.AppMainModuleBasic
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.runtime.FrontendWorkspace

open class BasicBrowserClientApplication(
    vararg modules: AppModule<FrontendWorkspace, BackendWorkspace>
) : BrowserApplication<FrontendWorkspace>() {

    init {
        this.modules += modules
    }

    override val backendMainKey: FragmentKey
        get() = AppMainModuleBasic.BASIC_CLIENT_BACKEND_MAIN_KEY

    override val frontendMainKey: FragmentKey
        get() = AppMainModuleBasic.BASIC_CLIENT_FRONTEND_MAIN_KEY

    override fun buildFrontendWorkspace() {
        frontendWorkspace = FrontendWorkspace(backend, backendWorkspace)
    }

    companion object {

        fun basicBrowserClient(start: Boolean = true, buildFun: ApplicationBuilder<FrontendWorkspace, BackendWorkspace>.() -> Unit) {
            val builder = ApplicationBuilder<FrontendWorkspace, BackendWorkspace>()

            builder.buildFun()

            if (start) {
                BasicBrowserClientApplication(*builder.modules.toTypedArray())
                    .also {
                        it.wireFormatProvider = builder.wireFormatProvider
                    }
                    .main()
            }
        }

    }
}