package `fun`.adaptive.value.util

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.value.*
import `fun`.adaptive.value.item.AvMarker

fun ServiceImpl<*>.serviceSubscribe(
    worker: AvValueWorker,
    subscriptionId: AvValueSubscriptionId,
    valueId: AvValueId,
): List<AvSubscribeCondition> {

    val conditions = listOf(AvSubscribeCondition(valueId = valueId))

    val subscription = AvClientSubscription(
        subscriptionId,
        conditions = conditions,
        transport = serviceContext.transport,
        scope = safeAdapter.scope
    )

    worker.subscribe(subscription)

    return conditions
}

fun ServiceImpl<*>.serviceSubscribe(
    worker: AvValueWorker,
    subscriptionId: AvValueSubscriptionId,
    vararg markers: AvMarker
): List<AvSubscribeCondition> {

    val conditions = markers.map { AvSubscribeCondition(marker = it) }

    val subscription = AvClientSubscription(
        subscriptionId,
        conditions = conditions,
        transport = serviceContext.transport,
        scope = safeAdapter.scope
    )

    worker.subscribe(subscription)

    return conditions
}