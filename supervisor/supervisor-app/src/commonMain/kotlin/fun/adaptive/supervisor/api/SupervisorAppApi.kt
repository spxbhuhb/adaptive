package `fun`.adaptive.supervisor.api

import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.supervisor.model.AppSpec
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueSubscriptionId
import `fun`.adaptive.value.item.AvMarker

@ServiceApi
interface SupervisorAppApi {

    suspend fun subscribe(subscriptionId: AvValueSubscriptionId): List<AvSubscribeCondition>

    suspend fun unsubscribe(id: AvValueSubscriptionId)

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