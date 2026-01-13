package `fun`.adaptive.app

import `fun`.adaptive.app.app.AppMainModuleBasic
import `fun`.adaptive.app.builder.ApplicationBuilder
import `fun`.adaptive.auth.api.AuthRoleApi
import `fun`.adaptive.auth.api.AuthSessionApi
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.runtime.AppAboutData
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Proto
import kotlinx.coroutines.runBlocking
import kotlin.collections.plusAssign

class JvmClientApplication(
    val url : String,
    val username : String,
    val password : String,
    vararg modules : AppModule<FrontendWorkspace, BackendWorkspace>
) : ClientApplication<FrontendWorkspace, BackendWorkspace>() {

    init {
        this.modules += modules
    }

    var wireFormatProvider : WireFormatProvider = Proto

    override lateinit var transport : ServiceCallTransport
    override lateinit var backend : BackendAdapter
    override lateinit var frontend : AdaptiveAdapter

    override val about : AppAboutData = AppAboutData()
    override val backendWorkspace = BackendWorkspace(this)
    override lateinit var frontendWorkspace : FrontendWorkspace

    override val backendMainKey: FragmentKey
        get() = AppMainModuleBasic.BASIC_CLIENT_BACKEND_MAIN_KEY

    override val frontendMainKey: FragmentKey
        get() = AppMainModuleBasic.BASIC_CLIENT_FRONTEND_MAIN_KEY

    fun main(mainFun : (JvmClientApplication) -> Unit) {
        runBlocking {

            moduleInit()

            wireFormatInit()

            transport = webSocketTransport(url, wireFormatProvider).also { it.start() }

            val sessionService = getService<AuthSessionApi>(transport)
            sessionService.signIn(username, password)

            genericSessionOrNull = getService<AuthSessionApi>(transport).getSession()
            if (genericSessionOrNull != null) {
                allApplicationRoles = getService<AuthRoleApi>(transport).all()
            }

            backend = backend(transport) { adapter ->
                backendAdapterInit(adapter)

                localContext(this@JvmClientApplication) {
                    actualize(backendMainKey, null)
                }
            }

            frontendWorkspace = FrontendWorkspace(backend, backendWorkspace)

            mainFun(this@JvmClientApplication)
        }
    }

    companion object {
        fun jvmClient(
            url : String,
            username : String,
            password : String,
            buildFun : ApplicationBuilder<FrontendWorkspace, BackendWorkspace>.() -> Unit,
            mainFun : (JvmClientApplication) -> Unit
        ) {
            val builder = ApplicationBuilder<FrontendWorkspace, BackendWorkspace>()

            builder.buildFun()

            JvmClientApplication(url, username, password, *builder.modules.toTypedArray()).main(mainFun)
        }

    }
}