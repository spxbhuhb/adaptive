package `fun`.adaptive.test

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.AdaptiveAdapter
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
import kotlinx.coroutines.launch

class TestClientApplication(
    val traceServiceCalls: Boolean = false,
    val server: TestServerApplication?
) : AbstractClientApplication<AbstractWorkspace, BackendWorkspace>() {

    override lateinit var transport: ServiceCallTransport
    override lateinit var backend: BackendAdapter
    override lateinit var frontend: AdaptiveAdapter

    override val backendWorkspace = BackendWorkspace(this)
    override val frontendWorkspace = NoFrontendWorkspace()

    override val frontendMainKey: FragmentKey
        get() = unsupported()

    override val backendMainKey: FragmentKey
        get() = unsupported()

    fun start(
        transport: ServiceCallTransport = DirectServiceTransport(name = "client", wireFormatProvider = Json),
        dispatcher: CoroutineDispatcher = Dispatchers.Unconfined,
        scope: CoroutineScope = CoroutineScope(dispatcher)
    ): TestClientApplication {

        if (server != null) {
            val serverTransport = server.backend.transport as? DirectServiceTransport
            val clientTransport = transport as? DirectServiceTransport

            if (serverTransport != null && clientTransport != null) {
                serverTransport.peerTransport = clientTransport
                clientTransport.peerTransport = serverTransport
            }
        }

        if (traceServiceCalls) {
            transport.trace = true
            transport.transportLog.enableFine()
        }

        scope.launch {

            moduleInit()

            wireFormatInit()

            loadResources()

            backendWorkspaceInit(backendWorkspace)

            backend = backend(transport, dispatcher = dispatcher, scope = scope) { adapter ->
                backendAdapterInit(adapter)

                localContext(this@TestClientApplication) {
                    for (s in backendWorkspace.services) {
                        service { s }
                    }

                    for (w in backendWorkspace.workers) {
                        worker { w }
                    }
                }
            }
        }

        return this
    }

    companion object {

        fun testClient(
            server: TestServerApplication? = null,
            trace: Boolean = false,
            start: Boolean = true,
            buildFun: TestClientBuilder.() -> Unit
        ): TestClientApplication {
            val builder = TestClientBuilder()

            builder.buildFun()

            return TestClientApplication(trace, server).also { if (start) it.start() }
        }

    }
}