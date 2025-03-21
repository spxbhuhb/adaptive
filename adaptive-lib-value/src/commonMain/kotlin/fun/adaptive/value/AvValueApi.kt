package `fun`.adaptive.value

import `fun`.adaptive.value.operation.AvValueOperation
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AvValueApi {

    suspend fun process(operation: AvValueOperation)

    suspend fun subscribe(conditions: List<AvSubscribeCondition> = emptyList()): AvValueSubscriptionId

    suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId)

}