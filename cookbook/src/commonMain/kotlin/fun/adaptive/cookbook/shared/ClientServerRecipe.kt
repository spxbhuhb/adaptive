package `fun`.adaptive.cookbook.shared

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.service.defaultServiceCallTransport
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.utility.delete
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

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

    fun Path.clean() : Path {
        check(this.toString().startsWith("./cookbook/var")) { "trying to clean a directory outside './cookbook/var'" }

        for (path in SystemFileSystem.list(this)) {
            if (SystemFileSystem.metadataOrNull(path)?.isDirectory == true) {
                path.clean()
            }
            path.delete()
        }

        return this
    }
}