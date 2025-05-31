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
import `fun`.adaptive.runtime.BackendWorkspace
import kotlinx.coroutines.runBlocking

class JvmServerApplication(
    vararg modules: AppModule<BackendWorkspace>
) : AbstractServerApplication<BackendWorkspace>() {

    init {
        this.modules += modules
    }

    override val workspace = BackendWorkspace()

    override val backendMainKey: FragmentKey
        get() = BasicAppServerModule.SERVER_BACKEND_MAIN_KEY

    override lateinit var backend: BackendAdapter

    fun main() {
        runBlocking {

            moduleInit()

            wireFormatInit()

            loadResources()

            workspaceInit(workspace)

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

        fun jvmServer(start: Boolean = true, buildFun: ApplicationBuilder<BackendWorkspace>.() -> Unit) {
            val builder = ApplicationBuilder<BackendWorkspace>()

            builder.buildFun()

            if (start) {
                JvmServerApplication(*builder.modules.toTypedArray()).main()
            }
        }

    }

}