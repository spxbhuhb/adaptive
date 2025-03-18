package `fun`.adaptive.iot.device

import `fun`.adaptive.iot.common.AioMarkerSet
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AioDeviceApi {

    suspend fun query(markers : AioMarkerSet = emptySet()) : List<AioDevice>

}