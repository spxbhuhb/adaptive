package `fun`.adaptive.value

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.operation.AvValueOperation

class AvValueServerService : ServiceImpl<AvValueServerService>(), AvValueApi {

    val worker by workerImpl<AvValueWorker>()

    override suspend fun process(operation: AvValueOperation) {
        throw UnsupportedOperationException()
    }

    override suspend fun subscribe(
        conditions: List<AvSubscribeCondition>,
        subscriptionId: AvSubscriptionId?
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