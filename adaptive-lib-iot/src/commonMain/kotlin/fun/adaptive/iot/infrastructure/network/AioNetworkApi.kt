package `fun`.adaptive.iot.infrastructure.network

import `fun`.adaptive.iot.common.AioMarkerSet
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AioNetworkApi {

    suspend fun query(markers : AioMarkerSet = emptySet()) : List<AioNetwork>

}