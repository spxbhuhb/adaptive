package `fun`.adaptive.iot.value

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.query.firstImpl

class AioValueTestClientService : ServiceImpl<AioValueTestClientService>, AioValueApi {

    override suspend fun update(value: AioValue) {
        adapter !!.firstImpl<AioValueWorker>().update(value)
    }

    override suspend fun subscribe(valueIds: List<AioValueId>): AuiValueSubscriptionId {
        throw UnsupportedOperationException()
    }

    override suspend fun unsubscribe(subscriptionId: AuiValueSubscriptionId) {
        throw UnsupportedOperationException()
    }

}