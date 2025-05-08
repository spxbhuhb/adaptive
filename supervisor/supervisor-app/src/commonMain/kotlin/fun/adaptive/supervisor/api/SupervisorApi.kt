package `fun`.adaptive.supervisor.api

import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.supervisor.model.AppSpec
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueSubscriptionId
import `fun`.adaptive.value.item.AvMarker

@ServiceApi
interface SupervisorApi {

    suspend fun subscribeApplications(): AvValueSubscriptionId

    suspend fun unsubscribe(id: AvValueSubscriptionId)

    suspend fun addApplication(
        name: String,
        spec: AppSpec,
        parentId: AvValueId? = null,
        markers: Map<AvMarker, AvValueId?>? = null
    )

    suspend fun updateApplication(id: AvValueId, spec: AppSpec)

    suspend fun removeApplication(id: AvValueId)

    suspend fun startApplication(id: AvValueId)

    suspend fun stopApplication(id: AvValueId)

}