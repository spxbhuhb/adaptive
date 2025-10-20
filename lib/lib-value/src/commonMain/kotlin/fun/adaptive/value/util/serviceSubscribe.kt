package `fun`.adaptive.value.util

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.value.*

fun ServiceImpl<*>.serviceSubscribe(
    worker: AvValueWorker,
    subscriptionId: AvSubscriptionId,
    valueId: AvValueId,
): List<AvSubscribeCondition> {

    val conditions = listOf(AvSubscribeCondition(valueId = valueId))

    val subscription = AvClientSubscription(
        subscriptionId,
        serviceContext.sessionRoles,
        conditions = conditions,
        transport = serviceContext.transport,
        scope = safeAdapter.scope
    )

    worker.subscribe(subscription)

    return conditions
}

fun ServiceImpl<*>.serviceSubscribe(
    worker: AvValueWorker,
    subscriptionId: AvSubscriptionId,
    vararg markers: AvMarker
): List<AvSubscribeCondition> {

    val conditions = markers.map { AvSubscribeCondition(marker = it) }

    val subscription = AvClientSubscription(
        subscriptionId,
        serviceContext.sessionRoles,
        conditions = conditions,
        transport = serviceContext.transport,
        scope = safeAdapter.scope
    )

    worker.subscribe(subscription)

    return conditions
}

fun ServiceImpl<*>.serviceSubscribe(
    worker: AvValueWorker,
    subscriptionId: AvSubscriptionId,
    conditions: List<AvSubscribeCondition>
): List<AvSubscribeCondition> {

    val subscription = AvClientSubscription(
        subscriptionId,
        serviceContext.sessionRoles,
        conditions = conditions,
        transport = serviceContext.transport,
        scope = safeAdapter.scope
    )

    worker.subscribe(subscription)

    return conditions
}