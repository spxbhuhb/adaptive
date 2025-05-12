package `fun`.adaptive.iot.device.publish

import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvSubscriptionId
import `fun`.adaptive.value.local.AvPublisher

@ServiceApi
interface AioDeviceTreePublishApi : AvPublisher {

    override suspend fun subscribe(subscriptionId: AvSubscriptionId): List<AvSubscribeCondition>

    override suspend fun unsubscribe(subscriptionId: AvSubscriptionId)

}