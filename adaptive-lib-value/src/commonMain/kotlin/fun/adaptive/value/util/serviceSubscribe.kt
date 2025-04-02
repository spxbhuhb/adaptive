package `fun`.adaptive.value.util

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.value.AvClientSubscription
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueSubscriptionId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvMarker

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