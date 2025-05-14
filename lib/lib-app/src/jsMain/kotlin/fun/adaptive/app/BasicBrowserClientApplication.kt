package `fun`.adaptive.app

import `fun`.adaptive.app.builder.ApplicationBuilder
import `fun`.adaptive.app.client.basic.BasicAppClientModule
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.ClientWorkspace

open class BasicBrowserClientApplication(
    vararg modules: AppModule<ClientWorkspace>
) : BrowserApplication<ClientWorkspace>() {

    init {
        this.modules += modules
    }

    override val backendMainKey: FragmentKey
        get() = BasicAppClientModule.BASIC_CLIENT_BACKEND_MAIN_KEY

    override val frontendMainKey: FragmentKey
        get() = BasicAppClientModule.BASIC_CLIENT_FRONTEND_MAIN_KEY

    override fun buildWorkspace() {
        workspace = ClientWorkspace(backend)
    }

    companion object {

        fun basicBrowserClient(start: Boolean = true, buildFun: ApplicationBuilder<ClientWorkspace>.() -> Unit) {
            val builder = ApplicationBuilder<ClientWorkspace>()

            builder.buildFun()

            if (start) {
                BasicBrowserClientApplication(*builder.modules.toTypedArray())
                    .also {
                        it.wireFormatProvider = builder.wireFormatProvider
                        it.localTransport = builder.localTransport
                    }
                    .main()
            }
        }

    }
}