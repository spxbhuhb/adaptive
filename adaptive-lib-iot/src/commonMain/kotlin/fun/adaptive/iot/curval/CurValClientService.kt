package `fun`.adaptive.iot.curval

import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.runtime.GlobalRuntimeContext

class CurValClientService : ServiceImpl<CurValClientService>, CurValApi {

    companion object {
        lateinit var worker : CurValWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isClient)
        worker = adapter!!.firstImpl<CurValWorker>()
    }

    override suspend fun update(curVal: CurVal) {
        publicAccess()
        worker.update(curVal)
    }

    override suspend fun subscribe(valueIds: List<AioValueId>) : CurValSubscriptionId {
        throw UnsupportedOperationException()
    }

    override suspend fun unsubscribe(subscriptionId: CurValSubscriptionId) {
        throw UnsupportedOperationException()
    }

}