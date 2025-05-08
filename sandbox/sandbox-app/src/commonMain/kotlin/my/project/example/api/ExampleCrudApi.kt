package my.project.example.api

import my.project.example.model.ExampleData
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueSubscriptionId
import `fun`.adaptive.value.item.AvMarker

@ServiceApi
interface ExampleCrudApi {

    suspend fun getAll(): List<ExampleData>

    suspend fun getById(id: AvValueId): ExampleData

    suspend fun put(
        name: String,
        spec: ExampleData,
        parentId: AvValueId? = null,
        markers: Map<AvMarker, AvValueId?>? = null
    )

    suspend fun delete(id: AvValueId)

    suspend fun subscribeAll(
        subscriptionId: AvValueSubscriptionId
    ): List<AvSubscribeCondition>

    suspend fun subscribeOne(
        subscriptionId: AvValueSubscriptionId,
        valueId: AvValueId
    ): List<AvSubscribeCondition>

    suspend fun unsubscribe(id: AvValueSubscriptionId)

}