package `fun`.adaptive.value

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.value.operation.AvValueOperation

class AvValueTestClientService : ServiceImpl<AvValueTestClientService>(), AvValueApi {

    override suspend fun process(operation: AvValueOperation) {
        adapter !!.firstImpl<AvValueWorker>().queue(operation)
    }

    override suspend fun subscribe(conditions: List<AvSubscribeCondition>): AvValueSubscriptionId {
        throw UnsupportedOperationException()
    }

    override suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId) {
        throw UnsupportedOperationException()
    }

}