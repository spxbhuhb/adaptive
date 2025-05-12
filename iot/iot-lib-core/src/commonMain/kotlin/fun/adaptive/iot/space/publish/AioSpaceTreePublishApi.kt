package `fun`.adaptive.iot.space.publish

import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvSubscriptionId
import `fun`.adaptive.value.local.AvPublisher

@ServiceApi
interface AioSpaceTreePublishApi : AvPublisher {

    override suspend fun subscribe(subscriptionId: AvSubscriptionId): List<AvSubscribeCondition>

    override suspend fun unsubscribe(subscriptionId: AvSubscriptionId)

}