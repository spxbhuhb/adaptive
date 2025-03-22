package `fun`.adaptive.iot.device

import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.iot.device.marker.AmvDevice
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AioDeviceApi {

    suspend fun add(name: String, itemType: AvMarker, parentId: AvValueId?): AvValueId

    suspend fun rename(deviceId: AvValueId, name: String)

    suspend fun moveUp(deviceId: AvValueId)

    suspend fun moveDown(deviceId: AvValueId)

    suspend fun getDeviceData(deviceId: AvValueId): AmvDevice

    suspend fun setDeviceData(valueId: AvValueId, notes: String?)

}