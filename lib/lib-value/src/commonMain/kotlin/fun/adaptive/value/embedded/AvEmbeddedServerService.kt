package `fun`.adaptive.value.embedded

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.AvClientSubscription
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvSubscriptionId
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueApi
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.operation.AvValueOperation

class AvEmbeddedServerService : ServiceImpl<AvEmbeddedServerService>(), AvValueApi {

    val worker by workerImpl<AvValueWorker>()

    override suspend fun get(avValueId: AvValueId): AvValue<*>? {
        return worker.get<Any>(avValueId)
    }

    override suspend fun process(operation: AvValueOperation) {
        worker.queue(operation)
    }

    override suspend fun subscribe(
        conditions: List<AvSubscribeCondition>,
        subscriptionId : AvSubscriptionId?
    ): AvSubscriptionId {

        val subscription = AvClientSubscription(
            subscriptionId ?: uuid4(),
            conditions,
            serviceContext.transport,
            safeAdapter.scope
        )

        worker.subscribe(subscription)

        return subscription.uuid
    }

    override suspend fun unsubscribe(subscriptionId: AvSubscriptionId) {
        worker.unsubscribe(subscriptionId)
    }

}