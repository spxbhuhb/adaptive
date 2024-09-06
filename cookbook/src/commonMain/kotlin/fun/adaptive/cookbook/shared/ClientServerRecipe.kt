package `fun`.adaptive.cookbook.shared

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.service.defaultServiceCallTransport
import `fun`.adaptive.service.testing.TestServiceTransport

abstract class ClientServerRecipe {

    abstract val serverBackend: BackendAdapter

    abstract val clientBackend: BackendAdapter

    suspend fun run() {

        defaultServiceCallTransport = TestServiceTransport(
            serviceImplFactory = serverBackend,
            peerTransport = TestServiceTransport(serviceImplFactory = clientBackend)
        )

        clientMain()
    }

    abstract suspend fun clientMain()

}