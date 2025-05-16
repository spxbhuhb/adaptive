package `fun`.adaptive.value

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.value.operation.AvValueOperation

class AvValueClientService : ServiceImpl<AvValueClientService>(), AvValueApi {

    val worker by workerImpl<AvValueWorker>()

    override suspend fun process(operation: AvValueOperation) {
        worker.queue(operation)
    }

    override suspend fun subscribe(
        conditions: List<AvSubscribeCondition>,
        subscriptionId: AvSubscriptionId?
    ): AvSubscriptionId {
        throw UnsupportedOperationException()
    }

    override suspend fun unsubscribe(subscriptionId: AvSubscriptionId) {
        throw UnsupportedOperationException()
    }

}