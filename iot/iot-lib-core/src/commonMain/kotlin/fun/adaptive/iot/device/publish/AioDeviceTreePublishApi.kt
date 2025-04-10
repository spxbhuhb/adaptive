package `fun`.adaptive.iot.device.publish

import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueSubscriptionId
import `fun`.adaptive.value.local.AvPublisher

@ServiceApi
interface AioDeviceTreePublishApi : AvPublisher {

    override suspend fun subscribe(subscriptionId: AvValueSubscriptionId): List<AvSubscribeCondition>

    override suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId)

}