package `fun`.adaptive.cookbook.auto.autoFile_autoFile

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.api.autoFile
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.cookbook.auto.AutoRecipe
import `fun`.adaptive.service.getService
import `fun`.adaptive.utility.ensure
import kotlinx.io.files.Path
import kotlin.time.Duration.Companion.seconds

/**
 * Create a permanent auto instance on the server side by a worker.
 *
 * - Persist the instance into a file.
 * - Connect to the instance from the client side from non-adaptive code.
 */
class Recipe : AutoRecipe() {

    val serverPath = Path(Path("./cookbook/var/auto/autoFile_autoFile/server").ensure().clean(), "item.json")
    val clientPath = Path(Path("./cookbook/var/auto/autoFile_autoFile/client").ensure().clean(), "item.json")

    override val serverBackend = backend(serverTransport) {
        auto()
        service { DataService() }
        worker { DataWorker(serverPath, trace = true) }
    }

    override val clientBackend = backend(clientTransport) {
        auto()
    }

    override suspend fun autoClientMain() {

        // Get the connection info. We have to do this before we create
        // the client side as the client side needs the client id
        // and the item id from `connectInfo.connectingHandle`.

        val connectInfo = getService<DataServiceApi>(clientTransport).getConnectInfo()

        // Create the client side auto file.

        val data = autoFile(
            clientBackend.firstImpl<AutoWorker>(),
            DataItem,
            clientPath,
            handle = connectInfo.connectingHandle
        )

        // Connect to the origin list on the server side. This call
        // will start synchronization between the two list. As the client
        // side is empty, it will load everything from the server side.

        data.connect(waitForSync = 2.seconds) { connectInfo }

        // Add an item on the client side

        data.frontend.modify("recordName", "record-name-2")

    }
}