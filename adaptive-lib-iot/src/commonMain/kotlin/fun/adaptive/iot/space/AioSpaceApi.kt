package `fun`.adaptive.iot.space

import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.iot.space.markers.AmvSpace
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AioSpaceApi {

    suspend fun add(name: String, spaceType: AvMarker, parentId: AvValueId?): AvValueId

    suspend fun rename(spaceId: AvValueId, name: String)

    suspend fun moveUp(spaceId: AvValueId)

    suspend fun moveDown(spaceId: AvValueId)

    suspend fun getSpaceData(spaceId: AvValueId): AmvSpace

    suspend fun setSpaceData(valueId: AvValueId, area: Double, notes: String?)

}