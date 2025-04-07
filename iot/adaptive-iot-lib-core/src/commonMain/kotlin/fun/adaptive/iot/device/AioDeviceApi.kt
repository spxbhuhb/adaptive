package `fun`.adaptive.iot.device

import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueSubscriptionId
import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.local.AvPublisher

@ServiceApi
interface AioDeviceApi : AvPublisher {

    override suspend fun subscribe(subscriptionId: AvValueSubscriptionId): List<AvSubscribeCondition>

    override suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId)

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