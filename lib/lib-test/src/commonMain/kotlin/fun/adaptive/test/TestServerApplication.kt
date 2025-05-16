package `fun`.adaptive.test

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.workerImpl
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
    vararg modules: AppModule<ServerWorkspace>
) : AbstractServerApplication<ServerWorkspace>() {

    init {
        this.modules += modules
    }

    override val workspace = ServerWorkspace()

    override lateinit var backend: BackendAdapter

    override val backendMainKey: FragmentKey
        get() = unsupported()

    fun start(
        transport: ServiceCallTransport = DirectServiceTransport(name = "node", wireFormatProvider = Json),
        dispatcher: CoroutineDispatcher = Dispatchers.Unconfined,
        scope: CoroutineScope = CoroutineScope(dispatcher)
    ) : TestServerApplication {
        GlobalRuntimeContext.nodeType = ApplicationNodeType.Server

        moduleInit()

        wireFormatInit()

        loadResources()

        workspaceInit(workspace)

        backend = backend(transport, dispatcher = dispatcher, scope = scope) { adapter ->
            backendAdapterInit(adapter)

            localContext(this@TestServerApplication) {
                for (s in workspace.services) {
                    service { s }
                }

                for (w in workspace.workers) {
                    workerImpl { w }
                }
            }
        }

        return this
    }

    companion object {

        fun testServer(buildFun: TestServerBuilder.() -> Unit): TestServerApplication {
            val builder = TestServerBuilder()

            builder.buildFun()

            return TestServerApplication(*builder.modules.toTypedArray())
        }

    }
}