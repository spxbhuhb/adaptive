package `fun`.adaptive.iot.space

import `fun`.adaptive.iot.item.AioMarker
import `fun`.adaptive.iot.space.markers.AmvSpace
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AioSpaceApi {

    suspend fun add(name: String, spaceType: AioMarker, parentId: AioValueId?): AioValueId

    suspend fun rename(spaceId : AioValueId, name: String)

    suspend fun moveUp(spaceId: AioValueId)

    suspend fun moveDown(spaceId: AioValueId)

    suspend fun getSpaceData(spaceId: AioValueId): AmvSpace

    suspend fun setSpaceData(valueId : AioValueId, area: Double, notes: String?)

}