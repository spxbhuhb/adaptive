package `fun`.adaptive.iot.value

import `fun`.adaptive.iot.value.operation.AioValueOperation
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AioValueApi {

    suspend fun process(operation: AioValueOperation)

    suspend fun subscribe(conditions: List<AioSubscribeCondition> = emptyList()): AioValueSubscriptionId

    suspend fun unsubscribe(subscriptionId: AioValueSubscriptionId)

}