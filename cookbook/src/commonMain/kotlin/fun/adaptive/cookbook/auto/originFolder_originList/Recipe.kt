package `fun`.adaptive.cookbook.auto.originFolder_originList

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.api.originList
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.cookbook.shared.ClientServerRecipe
import `fun`.adaptive.service.getService
import `fun`.adaptive.utility.UUID
import kotlinx.coroutines.delay

/**
 * Create a permanent auto list on the server side by a worker.
 *
 * - Persist the list into a folder.
 * - Connect to the list from the client side from non-adaptive code.
 */
class Recipe : ClientServerRecipe() {

    override val serverBackend = backend {
        auto()
        service { DataService() }
        worker { MasterDataWorker("./cookbook/var/auto", trace = true) }
    }

    override val clientBackend = backend {
        auto()
    }

    override suspend fun clientMain() {

        // Get the connection info. We have to do this before we create
        // the client side list as the client side list needs the client
        // id from `connectInfo.connectingHandle`.

        val connectInfo = getService<DataServiceApi>().getConnectInfo()

        // Create the client side list. This list is not persisted,
        // but from the application point of view it is permanent
        // as it is registered with `AutoWorker`.

        val data = originList(clientBackend.firstImpl<AutoWorker>(), MasterDataItem, handle = connectInfo.connectingHandle, trace = true)

        // Connect to the origin list on the server side. This call
        // will start synchronization between the two list. As the client
        // side is empty, it will load everything from the server side.

        data.connect { connectInfo }

        // Wait until the initial synchronization finishes



        // Add an item on the client side

        data.frontend.add(MasterDataItem(UUID(), "record-name-1"))

    }

}