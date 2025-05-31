package `fun`.adaptive.app

import `fun`.adaptive.app.builder.ApplicationBuilder
import `fun`.adaptive.app.server.BasicAppServerModule
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.AbstractServerApplication
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.runtime.NoFrontendWorkspace
import kotlinx.coroutines.runBlocking

class JvmServerApplication(
    vararg modules: AppModule<AbstractWorkspace, BackendWorkspace>
) : AbstractServerApplication<AbstractWorkspace, BackendWorkspace>() {

    init {
        this.modules += modules
    }

    override val frontendWorkspace = NoFrontendWorkspace()
    override val backendWorkspace = BackendWorkspace()

    override val backendMainKey: FragmentKey
        get() = BasicAppServerModule.SERVER_BACKEND_MAIN_KEY

    override lateinit var backend: BackendAdapter

    fun main() {
        runBlocking {

            moduleInit()

            wireFormatInit()

            loadResources()

            backendWorkspaceInit(backendWorkspace)

            backend = backend { adapter ->
                backendAdapterInit(adapter)

                localContext(this@JvmServerApplication) {
                    actualize(backendMainKey)
                }
            }

            // at this point all backend components are created and mounted
            // so it is safe to use the database

            Runtime.getRuntime().addShutdownHook(Thread { backend.stop() })

            while (backend.isRunning) {
                Thread.sleep(1000)
            }
        }
    }

    companion object {

        fun jvmServer(start: Boolean = true, buildFun: ApplicationBuilder<AbstractWorkspace, BackendWorkspace>.() -> Unit) {
            val builder = ApplicationBuilder<AbstractWorkspace, BackendWorkspace>()

            builder.buildFun()

            if (start) {
                JvmServerApplication(*builder.modules.toTypedArray()).main()
            }
        }

    }

}