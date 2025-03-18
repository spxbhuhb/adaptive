package `fun`.adaptive.iot.item

import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AioItemApi {

    suspend fun queryByMarker(marker: AioMarker): List<AioItem>

    suspend fun add(item: AioItem, markerData: Map<AioMarker, Any?>)

}