package `fun`.adaptive.value.remote

import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvSubscriptionId

interface AvPublisher {

    suspend fun subscribe(subscriptionId: AvSubscriptionId): List<AvSubscribeCondition>

    suspend fun unsubscribe(subscriptionId: AvSubscriptionId)

}