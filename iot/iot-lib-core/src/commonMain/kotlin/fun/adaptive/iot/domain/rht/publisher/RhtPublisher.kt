package `fun`.adaptive.iot.domain.rht.publisher

import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueSubscriptionId
import `fun`.adaptive.value.local.AvMarkedValueId
import `fun`.adaptive.value.local.AvPublisher

class RhtPublisher(
    val service: RhtPublishApi,
    val spaceIds: List<AvValueId>
) : AvPublisher {

    lateinit var mapping: Map<AvValueId, List<AvMarkedValueId>>

    override suspend fun subscribe(subscriptionId: AvValueSubscriptionId): List<AvSubscribeCondition> {
        val result = service.subscribe(subscriptionId, spaceIds)
        mapping = result.mapping
        return result.conditions
    }

    override suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId) {
        service.unsubscribe(subscriptionId)
    }

}