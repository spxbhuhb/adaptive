package `fun`.adaptive.iot.domain.rht.publisher

import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvSubscriptionId
import `fun`.adaptive.value.local.AvMarkedValueSubscriptionResult

@ServiceApi
interface RhtPublishApi {

    suspend fun subscribe(subscriptionId: AvSubscriptionId, spaceIds : List<AvValueId>): AvMarkedValueSubscriptionResult

    suspend fun unsubscribe(subscriptionId: AvSubscriptionId)

}