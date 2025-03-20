package `fun`.adaptive.iot.space

import `fun`.adaptive.iot.item.AioMarker
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AioSpaceApi {

    suspend fun addSpace(name : String, spaceType : AioMarker, parentId: AioValueId?) : AioValueId

}