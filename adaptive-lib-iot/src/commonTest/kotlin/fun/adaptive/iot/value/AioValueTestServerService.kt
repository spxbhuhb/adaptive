package `fun`.adaptive.iot.value

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.iot.value.operation.AioValueOperation
import `fun`.adaptive.utility.UUID.Companion.uuid4

class AioValueTestServerService : ServiceImpl<AioValueTestServerService>, AioValueApi {

    val worker
        get() = safeAdapter.firstImpl<AioValueWorker>()

    override suspend fun process(operation: AioValueOperation) {
        worker.process(operation)
    }

    override suspend fun subscribe(conditions: List<AioSubscribeCondition>): AuiValueSubscriptionId {

        val subscription = AioValueClientSubscription(
            uuid4(),
            conditions,
            serviceContext.transport,
            safeAdapter.scope
        )

        worker.subscribe(subscription)

        return subscription.uuid
    }

    override suspend fun unsubscribe(subscriptionId: AuiValueSubscriptionId) {
        worker.unsubscribe(subscriptionId)
    }

}