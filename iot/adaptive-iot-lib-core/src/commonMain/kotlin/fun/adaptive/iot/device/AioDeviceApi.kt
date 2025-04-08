package `fun`.adaptive.iot.device

import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvMarker

@ServiceApi
interface AioDeviceApi {

    suspend fun add(
        name: String,
        itemType: AvMarker,
        parentId: AvValueId?,
        spec: AioDeviceSpec,
        markers: Map<AvMarker, AvValueId?>? = null
    ): AvValueId

    suspend fun rename(deviceId: AvValueId, name: String)

    suspend fun moveUp(deviceId: AvValueId)

    suspend fun moveDown(deviceId: AvValueId)

    suspend fun setSpec(valueId: AvValueId, spec: AioDeviceSpec)

}