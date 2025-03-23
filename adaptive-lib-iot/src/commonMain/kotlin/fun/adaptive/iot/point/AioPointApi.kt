package `fun`.adaptive.iot.point

import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvMarker

@ServiceApi
interface AioPointApi {

    suspend fun add(name: String, itemType: AvMarker, parentId: AvValueId, spec : AioPointSpec): AvValueId

    suspend fun rename(valueId: AvValueId, name: String)

    suspend fun moveUp(valueId: AvValueId)

    suspend fun moveDown(valueId: AvValueId)

    suspend fun setSpec(valueId: AvValueId, spec : AioPointSpec)

}