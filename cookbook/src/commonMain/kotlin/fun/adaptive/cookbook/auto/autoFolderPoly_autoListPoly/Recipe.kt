package `fun`.adaptive.cookbook.auto.autoFolderPoly_autoListPoly

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.api.autoListPoly
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.cookbook.auto.AutoRecipe
import `fun`.adaptive.service.getService
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.wireformat.WireFormatRegistry
import kotlinx.datetime.Clock.System.now
import kotlinx.io.files.Path
import kotlin.time.Duration.Companion.seconds

/**
 * Create a permanent auto list on the server side by a worker.
 *
 * - Persist the list into a folder.
 * - Connect to the list from the client side from non-adaptive code.
 */
class Recipe : AutoRecipe() {

    val serverPath = Path("./cookbook/var/auto/autoFolderPoly_autoListPoly").ensure().clean()

    companion object {
        init {
            WireFormatRegistry.plusAssign(StringItem)
            WireFormatRegistry.plusAssign(IntItem)
            WireFormatRegistry.plusAssign(InstantItem)
        }
    }

    override val serverBackend = backend {
        auto()
        service { DataService() }
        worker { MasterDataWorker(serverPath) }
    }

    override val clientBackend = backend {
        auto()
    }

    override suspend fun autoClientMain() {

        // Get the connection info. We have to do this before we create
        // the client side list as the client side list needs the client
        // id from `connectInfo.connectingHandle`.

        val connectInfo = getService<DataServiceApi>().getConnectInfo()

        // Create the client side list. This list is not persisted,
        // but from the application point of view it is permanent
        // as it is registered with `AutoWorker`.

        val data = autoListPoly<AdatClass>(
            clientBackend.firstImpl<AutoWorker>(),
            handle = connectInfo.connectingHandle
        )

        // Connect to the origin list on the server side. This call
        // will start synchronization between the two list. As the client
        // side is empty, it will load everything from the server side.

        data.connect(waitForSync = 2.seconds) { connectInfo }

        // Add an item on the client side

        val fe = data.frontend

        fe.add(StringItem(UUID(), "record-name-1"))
        fe.add(IntItem(UUID(), 12))
        fe.add(InstantItem(UUID(), now()))

    }

}