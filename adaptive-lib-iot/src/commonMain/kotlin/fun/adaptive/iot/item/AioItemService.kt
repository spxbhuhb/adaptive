package `fun`.adaptive.iot.item

import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.iot.value.AioValue
import `fun`.adaptive.iot.value.AioValueWorker
import kotlinx.datetime.Clock.System.now

class AioItemService : ServiceImpl<AioItemService>, AioItemApi {

    companion object {
        lateinit var worker: AioValueWorker
    }

    override fun mount() {
        worker = safeAdapter.firstImpl<AioValueWorker>()
    }

    override suspend fun queryByMarker(marker: AioMarker): List<AioItem> {
        publicAccess()
        TODO()
//        return worker.query { it.value is AioItem && marker in it.value.markers }.map { it.value as AioItem }
    }

//    override suspend fun add(item: AioItem, markerData: Map<AioMarker, Any?>) {
//        publicAccess()
//        //worker.update(AioValue(item.uuid.cast(), now(), item.status, item))
//    }

}