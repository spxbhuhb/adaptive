package `fun`.adaptive.value

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.value.operation.AvValueOperation
import `fun`.adaptive.runtime.GlobalRuntimeContext

class AvValueClientService : ServiceImpl<AvValueClientService>, AvValueApi {

    companion object {
        lateinit var worker: AvValueWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isClient)
        worker = adapter !!.firstImpl<AvValueWorker>()
    }

    override suspend fun process(operation: AvValueOperation) {
        worker.queue(operation)
    }

    override suspend fun subscribe(conditions: List<AvSubscribeCondition>): AvValueSubscriptionId {
        throw UnsupportedOperationException()
    }

    override suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId) {
        throw UnsupportedOperationException()
    }

}