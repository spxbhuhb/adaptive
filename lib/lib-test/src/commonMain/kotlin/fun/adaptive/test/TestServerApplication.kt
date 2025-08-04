package `fun`.adaptive.test

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.runtime.*
import `fun`.adaptive.service.testing.DirectServiceTransport
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.wireformat.api.Json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class TestServerApplication(
    val builder: TestServerBuilder
) : AbstractServerApplication<AbstractWorkspace, BackendWorkspace>() {

    init {
        this.modules += builder.modules
    }

    override val about = AppAboutData()

    override val frontendWorkspace: NoFrontendWorkspace
        get() = unsupported()

    override val backendWorkspace = BackendWorkspace(this)

    override lateinit var backend: BackendAdapter

    override val backendMainKey: FragmentKey
        get() = unsupported()

    fun start(
        transport: ServiceCallTransport = DirectServiceTransport(name = "server", wireFormatProvider = Json),
        dispatcher: CoroutineDispatcher = Dispatchers.Unconfined,
        scope: CoroutineScope = CoroutineScope(dispatcher)
    ): TestServerApplication {

        if (builder.traceServiceCalls) {
            transport.trace = true
            transport.transportLog.enableFine()
        }

        moduleInit()

        wireFormatInit()

        loadResources()

        backendWorkspaceInit(backendWorkspace)

        backend = backend(transport, dispatcher = dispatcher, scope = scope) { adapter ->
            backendAdapterInit(adapter)

            localContext(this@TestServerApplication) {
                for (s in backendWorkspace.services) {
                    service { s }
                }

                for (w in backendWorkspace.workers) {
                    worker { w }
                }
            }
        }

        return this
    }

    companion object {

        fun testServer(
            trace: Boolean = false,
            start: Boolean = true,
            buildFun: TestServerBuilder.() -> Unit
        ): TestServerApplication {
            val builder = TestServerBuilder()

            builder.traceServiceCalls = trace
            builder.buildFun()

            return TestServerApplication(builder).also { if (start) it.start() }
        }

    }
}