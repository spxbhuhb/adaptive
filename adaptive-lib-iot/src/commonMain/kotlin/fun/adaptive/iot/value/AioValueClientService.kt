package `fun`.adaptive.iot.value

import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.runtime.GlobalRuntimeContext

class AioValueClientService : ServiceImpl<AioValueClientService>, AioValueApi {

    companion object {
        lateinit var worker: AioValueWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isClient)
        worker = adapter !!.firstImpl<AioValueWorker>()
    }

    override suspend fun update(value: AioValue) {
        publicAccess()
        worker.update(value)
    }

    override suspend fun subscribe(valueIds: List<AioValueId>): AuiValueSubscriptionId {
        throw UnsupportedOperationException()
    }

    override suspend fun unsubscribe(subscriptionId: AuiValueSubscriptionId) {
        throw UnsupportedOperationException()
    }

}