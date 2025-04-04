package `fun`.adaptive.value.local

import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueSubscriptionId

interface AvPublisher {

    suspend fun subscribe(subscriptionId: AvValueSubscriptionId, valueId : AvValueId): List<AvSubscribeCondition>

    suspend fun subscribeAll(subscriptionId: AvValueSubscriptionId): List<AvSubscribeCondition>

    suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId)

}