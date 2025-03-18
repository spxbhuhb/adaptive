package `fun`.adaptive.iot.value

import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AioValueApi {

    suspend fun update(value: AioValue)

    suspend fun subscribe(valueIds: List<AioValueId>): AuiValueSubscriptionId

    suspend fun unsubscribe(subscriptionId: AuiValueSubscriptionId)

}