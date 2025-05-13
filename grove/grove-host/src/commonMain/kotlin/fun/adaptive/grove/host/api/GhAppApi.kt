package `fun`.adaptive.grove.host.api

import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.grove.host.model.AppSpec
import `fun`.adaptive.value.AvMarker
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvSubscriptionId

@ServiceApi
interface GhAppApi {

    suspend fun subscribe(subscriptionId: AvSubscriptionId): List<AvSubscribeCondition>

    suspend fun unsubscribe(id: AvSubscriptionId)

    suspend fun save(
        name: String,
        spec: AppSpec,
        parentId: AvValueId? = null,
        markers: Map<AvMarker, AvValueId?>? = null
    )

    suspend fun remove(id: AvValueId)

    suspend fun start(id: AvValueId)

    suspend fun stop(id: AvValueId)

}