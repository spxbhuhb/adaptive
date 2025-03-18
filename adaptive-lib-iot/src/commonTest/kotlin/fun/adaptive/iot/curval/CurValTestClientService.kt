package `fun`.adaptive.iot.curval

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.query.firstImpl

class CurValTestClientService : ServiceImpl<CurValTestClientService>, CurValApi {

    override suspend fun update(curVal: CurVal) {
        adapter!!.firstImpl<CurValWorker>().update(curVal)
    }

    override suspend fun subscribe(valueIds: List<AioValueId>) : CurValSubscriptionId {
        throw UnsupportedOperationException()
    }

    override suspend fun unsubscribe(subscriptionId: CurValSubscriptionId) {
        throw UnsupportedOperationException()
    }

}