package `fun`.adaptive.iot.value

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.iot.value.operation.AioValueOperation

class AioValueTestClientService : ServiceImpl<AioValueTestClientService>, AioValueApi {

    override suspend fun process(operation: AioValueOperation) {
        adapter !!.firstImpl<AioValueWorker>().queueOperation(operation)
    }

    override suspend fun subscribe(conditions: List<AioSubscribeCondition>): AioValueSubscriptionId {
        throw UnsupportedOperationException()
    }

    override suspend fun unsubscribe(subscriptionId: AioValueSubscriptionId) {
        throw UnsupportedOperationException()
    }

}