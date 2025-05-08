package `fun`.adaptive.value

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.operation.AvValueOperation

class AvValueServerService() : ServiceImpl<AvValueServerService>(), AvValueApi {

    companion object {
        lateinit var worker: AvValueWorker
        lateinit var domain: AvValueDomain
        lateinit var authCheck : ServiceImpl<*>.() -> Unit
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)
        worker = safeAdapter.firstImpl<AvValueWorker> { it.domain == domain }
    }

    override suspend fun process(operation: AvValueOperation) {
        throw UnsupportedOperationException()
    }

    override suspend fun subscribe(conditions: List<AvSubscribeCondition>): AvValueSubscriptionId {
        authCheck()

        val subscription = AvClientSubscription(
            uuid4(),
            conditions,
            serviceContext.transport,
            safeAdapter.scope
        )

        worker.subscribe(subscription)

        return subscription.uuid
    }

    override suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId) {
        authCheck()

        worker.unsubscribe(subscriptionId)
    }

}