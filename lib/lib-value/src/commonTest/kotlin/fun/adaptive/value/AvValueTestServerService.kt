package `fun`.adaptive.value

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.value.operation.AvValueOperation
import `fun`.adaptive.utility.UUID.Companion.uuid4

class AvValueTestServerService : ServiceImpl<AvValueTestServerService>(), AvValueApi {

    val worker
        get() = safeAdapter.firstImpl<AvValueWorker>()

    override suspend fun process(operation: AvValueOperation) {
        worker.queue(operation)
    }

    override suspend fun subscribe(conditions: List<AvSubscribeCondition>): AvValueSubscriptionId {

        val subscription = AvClientSubscription(
            uuid4(),
            conditions,
            serviceContext.transport,
            safeAdapter.scope
        )

        worker.subscribe(subscription)

        return subscription.uuid
    }

    override suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId) {
        worker.unsubscribe(subscriptionId)
    }

}