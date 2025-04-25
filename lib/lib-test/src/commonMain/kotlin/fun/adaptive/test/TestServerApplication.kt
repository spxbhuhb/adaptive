package `fun`.adaptive.test

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.runtime.*

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

    fun start() {
        GlobalRuntimeContext.nodeType = ApplicationNodeType.Server

        moduleInit()

        wireFormatInit()

        loadResources()

        workspaceInit(workspace)

        backend = backend { adapter ->
            backendAdapterInit(adapter)

            localContext(this@TestServerApplication) {
                for (s in workspace.services) {
                    service { s }
                }

                for (w in workspace.workers) {
                    worker { w }
                }
            }
        }
    }

    companion object {

        fun testServer(start: Boolean = true, buildFun: TestServerBuilder.() -> Unit) : TestServerApplication {
            val builder = TestServerBuilder()

            builder.buildFun()

            TestServerApplication(*builder.modules.toTypedArray()).also {
                if (start) it.start()
                return it
            }
        }

    }
}