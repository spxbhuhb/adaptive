package `fun`.adaptive.value

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.service.ServiceProvider
import `fun`.adaptive.service.auth.AccessDenied
import `fun`.adaptive.service.auth.ensureHas
import `fun`.adaptive.service.auth.ensureLoggedIn
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.operation.AvValueOperation

@ServiceProvider
class AvValueServerService : ServiceImpl<AvValueServerService>(), AvValueApi {

    val worker by workerImpl<AvValueWorker>()

    override suspend fun get(avValueId: AvValueId): AvValue<*>? {
        ensureLoggedIn()

        val value = worker.get<Any>(avValueId)
        if (value.acl == null) return value
        ensureHas(value.acl)
        return value
    }

    override suspend fun get(marker: AvMarker): List<AvValue<*>> {
        ensureLoggedIn()

        val roles = serviceContext.session.roles
        return worker.get<Any>(marker).filter { it.acl == null || roles.contains(it.acl) }
    }

    override suspend fun put(avValue: AvValue<*>) {
        throw UnsupportedOperationException()
    }

    override suspend fun process(operation: AvValueOperation) {
        throw UnsupportedOperationException()
    }

    override suspend fun subscribe(
        conditions: List<AvSubscribeCondition>,
        subscriptionId: AvSubscriptionId?
    ): AvSubscriptionId {
        ensureLoggedIn()

        val subscription = AvClientSubscription(
            subscriptionId ?: uuid4(),
            serviceContext.sessionRoles,
            conditions,
            serviceContext.transport,
            safeAdapter.scope
        )

        worker.subscribe(subscription)

        return subscription.uuid
    }

    override suspend fun unsubscribe(subscriptionId: AvSubscriptionId) {
        ensureLoggedIn()

        worker.unsubscribe(subscriptionId)
    }

}