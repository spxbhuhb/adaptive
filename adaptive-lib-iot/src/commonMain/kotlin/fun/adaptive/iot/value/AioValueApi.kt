package `fun`.adaptive.iot.value

import `fun`.adaptive.iot.item.AioMarker
import `fun`.adaptive.iot.value.operation.AioValueOperation
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AioValueApi {

    suspend fun process(operation: AioValueOperation)

    suspend fun subscribe(valueIds: List<AioValueId> = emptyList(), markerIds: List<AioMarker> = emptyList()): AuiValueSubscriptionId

    suspend fun unsubscribe(subscriptionId: AuiValueSubscriptionId)

}