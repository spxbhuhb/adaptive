package `fun`.adaptive.iot.value

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.utility.UUID.Companion.uuid4

class AioValueTestServerService : ServiceImpl<AioValueTestServerService>, AioValueApi {

    val worker
        get() = adapter !!.firstImpl<AioValueWorker>()

    override suspend fun update(value: AioValue) {
        worker.update(value)
    }

    override suspend fun subscribe(valueIds: List<AioValueId>): AuiValueSubscriptionId {

        val subscription = AioValueClientSubscription(
            uuid4(),
            valueIds,
            serviceContext.transport,
            adapter !!.scope
        )

        worker.subscribe(subscription)

        return subscription.uuid
    }

    override suspend fun unsubscribe(subscriptionId: AuiValueSubscriptionId) {
        worker.unsubscribe(subscriptionId)
    }

}