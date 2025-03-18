package `fun`.adaptive.iot.curval

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.utility.UUID.Companion.uuid4

class CurValTestServerService : ServiceImpl<CurValTestServerService>, CurValApi {

    val worker
        get() = adapter!!.firstImpl<CurValWorker>()

    override suspend fun update(curVal: CurVal) {
        worker.update(curVal)
    }

    override suspend fun subscribe(valueIds: List<AioValueId>): CurValSubscriptionId {

        val subscription = CurValClientSubscription(
            uuid4(),
            valueIds,
            serviceContext.transport,
            adapter !!.scope
        )

        worker.subscribe(subscription)

        return subscription.uuid
    }

    override suspend fun unsubscribe(subscriptionId: CurValSubscriptionId) {
        worker.unsubscribe(subscriptionId)
    }

}