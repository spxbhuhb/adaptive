package `fun`.adaptive.value.testing

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvSubscriptionId
import `fun`.adaptive.value.AvValueApi
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.operation.AvValueOperation

class AvEmbeddedClientService : ServiceImpl<AvEmbeddedClientService>(), AvValueApi {

    override suspend fun process(operation: AvValueOperation) {
        adapter !!.firstImpl<AvValueWorker>().queue(operation)
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