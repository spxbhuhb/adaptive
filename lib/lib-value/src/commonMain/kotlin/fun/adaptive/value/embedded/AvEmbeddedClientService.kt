package `fun`.adaptive.value.embedded

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvSubscriptionId
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueApi
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.operation.AvValueOperation

class AvEmbeddedClientService : ServiceImpl<AvEmbeddedClientService>(), AvValueApi {

    val worker by workerImpl<AvValueWorker>()

    override suspend fun get(avValueId: AvValueId): AvValue<*>? {
        throw UnsupportedOperationException()
    }

    override suspend fun process(operation: AvValueOperation) {
        worker.queue(operation)
    }

    override suspend fun subscribe(
        conditions: List<AvSubscribeCondition>,
        subscriptionId : AvSubscriptionId?
    ): AvSubscriptionId {
        throw UnsupportedOperationException()
    }

    override suspend fun unsubscribe(subscriptionId: AvSubscriptionId) {
        throw UnsupportedOperationException()
    }

}