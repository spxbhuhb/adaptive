package `fun`.adaptive.iot.space

import `fun`.adaptive.iot.item.AioMarker
import `fun`.adaptive.iot.space.markers.AmvSpace
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AioSpaceApi {

    suspend fun addSpace(name : String, spaceType : AioMarker, parentId: AioValueId?) : AioValueId

    suspend fun moveUp(spaceId: AioValueId)

    suspend fun moveDown(spaceId: AioValueId)

    suspend fun spaceData(spaceId: AioValueId): AmvSpace

}