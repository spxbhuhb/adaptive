package `fun`.adaptive.value

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.operation.AvValueOperation

class AvValueServerService : ServiceImpl<AvValueServerService>(), AvValueApi {

    val worker by workerImpl<AvValueWorker>()

    override suspend fun get(avValueId: AvValueId): AvValue<*>? {
        return worker.get<Any>(avValueId)
    }

    override suspend fun get(marker: AvMarker): List<AvValue<*>> {
        return worker.get<Any>(marker)
    }

    override suspend fun put(avValue: AvValue<*>) {
        worker.execute { this += avValue }
    }

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