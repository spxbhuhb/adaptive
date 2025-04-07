package `fun`.adaptive.iot.space

import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueSubscriptionId
import `fun`.adaptive.value.local.AvPublisher

@ServiceApi
interface AioSpaceApi : AvPublisher {

    override suspend fun subscribe(subscriptionId: AvValueSubscriptionId): List<AvSubscribeCondition>

    override suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId)

    suspend fun add(name: String, spaceType: AvMarker, parentId: AvValueId?): AvValueId

    suspend fun rename(spaceId: AvValueId, name: String)

    suspend fun moveUp(spaceId: AvValueId)

    suspend fun moveDown(spaceId: AvValueId)

    suspend fun setSpecSpec(valueId: AvValueId, spec : AioSpaceSpec)

    /**
     * Add a `spaceRef` marker to [itemId] that points to [spaceId].
     * Add the [itemId] to the [listMarker] marker list of [spaceId].
     */
    suspend fun setSpace(itemId: AvValueId, spaceId: AvValueId, listMarker : AvMarker)

}