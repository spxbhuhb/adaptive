package `fun`.adaptive.app

import `fun`.adaptive.app.builder.ApplicationBuilder
import `fun`.adaptive.app.app.AppMainModuleServer
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.AbstractServerApplication
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppAboutData
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.runtime.NoFrontendWorkspace
import kotlinx.coroutines.runBlocking

class JvmServerApplication(
    override val about: AppAboutData,
    vararg modules: AppModule<AbstractWorkspace, BackendWorkspace>
) : AbstractServerApplication<AbstractWorkspace, BackendWorkspace>() {

    init {
        this.modules += modules
    }

    val logger = getLogger(this::class.simpleName!!)

    override val frontendWorkspace = NoFrontendWorkspace()
    override val backendWorkspace = BackendWorkspace(this)

    override val backendMainKey: FragmentKey
        get() = AppMainModuleServer.SERVER_BACKEND_MAIN_KEY

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
                    actualize(backendMainKey, null)
                }
            }

            // at this point all backend components are created and mounted
            // so it is safe to use the database

            Runtime.getRuntime().addShutdownHook(Thread {
                logger.info { "shutdown request received, stopping application" }
                backend.stop()
                logger.info { "application has been stopped" }
            })

            logger.info { "the application is running" }

            while (backend.isRunning) {
                Thread.sleep(1000)
            }
        }
    }

    companion object {

        fun jvmServer(
            about: AppAboutData = AppAboutData(),
            start: Boolean = true,
            buildFun: ApplicationBuilder<AbstractWorkspace, BackendWorkspace>.() -> Unit
        ) {
            val builder = ApplicationBuilder<AbstractWorkspace, BackendWorkspace>()

            builder.buildFun()

            if (start) {
                JvmServerApplication(about, *builder.modules.toTypedArray()).main()
            }
        }

    }

}