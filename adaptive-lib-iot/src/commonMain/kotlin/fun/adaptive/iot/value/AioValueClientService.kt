package `fun`.adaptive.iot.value

import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.value.operation.AioValueOperation
import `fun`.adaptive.runtime.GlobalRuntimeContext

class AioValueClientService : ServiceImpl<AioValueClientService>, AioValueApi {

    companion object {
        lateinit var worker: AioValueWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isClient)
        worker = adapter !!.firstImpl<AioValueWorker>()
    }

    override suspend fun process(operation: AioValueOperation) {
        publicAccess()
        worker.process(operation)
    }

    override suspend fun subscribe(conditions: List<AioSubscribeCondition>): AuiValueSubscriptionId {
        throw UnsupportedOperationException()
    }

    override suspend fun unsubscribe(subscriptionId: AuiValueSubscriptionId) {
        throw UnsupportedOperationException()
    }

}