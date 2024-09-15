package `fun`.adaptive.cookbook.auto.originListPoly_originFolderPoly_originListPoly

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.api.originListPoly
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.internal.origin.OriginBase
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.cookbook.shared.ClientServerRecipe
import `fun`.adaptive.service.getService
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.waitFor
import `fun`.adaptive.wireformat.WireFormatRegistry
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.seconds

/**
 * Create a permanent auto list on the server side by a worker.
 *
 * - Persist the list into a folder.
 * - Connect to the list from the client side from non-adaptive code.
 */
class Recipe : ClientServerRecipe() {

    companion object {
        init {
            WireFormatRegistry += StringItem
            WireFormatRegistry += IntItem
            WireFormatRegistry += InstantItem
        }
    }

    override val serverBackend = backend {
        auto()
        service { DataService() }
        worker { MasterDataWorker("./cookbook/var/auto/originListPoly_originFolderPoly_originListPoly", trace = true) }
    }

    override val clientBackend = backend {
        auto()
    }

    override suspend fun clientMain() {

        // Wait for the worker to initialise the data. This happens in the background in
        // a coroutine, so we have to wait for it.

        val masterDataWorker = serverBackend.firstImpl<MasterDataWorker>()

        waitFor(1.seconds) { masterDataWorker.masterDataOrNull != null }

        val (client1, frontend1) = client()
        val (client2, frontend2) = client()

        frontend1.add(StringItem(UUID(), "record-name-1"))
        frontend2.add(StringItem(UUID(), "record-name-2"))

        frontend1.add(IntItem(UUID(), 12))
        frontend2.add(IntItem(UUID(), 23))

        frontend1.add(InstantItem(UUID(), now()))
        frontend2.add(InstantItem(UUID(), now()))

        // Wait for sync on the server side. Typically, we don't wait this way
        // but for the cookbook recipe it is needed so the program has time
        // to send the data and write out the file.

        waitFor(2.seconds) { masterDataWorker.masterData.backend.context.time.timestamp == client1.backend.context.time.timestamp }
        waitFor(2.seconds) { masterDataWorker.masterData.backend.context.time.timestamp == client2.backend.context.time.timestamp }

    }

    suspend fun client(): Pair<OriginBase<*, *,List<AdatClass>>, AdatClassListFrontend<AdatClass>> {
        // Get the connection info. We have to do this before we create
        // the client side list as the client side list needs the client
        // id from `connectInfo.connectingHandle`.

        val connectInfo = getService<DataServiceApi>().getConnectInfo()

        // Create the client side list. This list is not persisted,
        // but from the application point of view it is permanent
        // as it is registered with `AutoWorker`.

        val data = originListPoly(clientBackend.firstImpl<AutoWorker>(), StringItem, handle = connectInfo.connectingHandle, trace = true)

        // Connect to the origin list on the server side. This call
        // will start synchronization between the two list. As the client
        // side is empty, it will load everything from the server side.

        data.connect(waitForSync = 2.seconds) { connectInfo }

        return data to data.frontend
    }

}