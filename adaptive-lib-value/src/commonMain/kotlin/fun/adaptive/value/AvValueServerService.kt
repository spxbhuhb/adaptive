package `fun`.adaptive.value

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.trimSignature
import `fun`.adaptive.value.operation.AvValueOperation

class AvValueServerService : ServiceImpl<AvValueServerService>, AvValueApi {

    companion object {
        val signature = AvValueServerService.typeSignature().trimSignature()

        // lateinit var queryRole: RoleId
        // lateinit var updateRole: RoleId

        lateinit var worker: AvValueWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)

        // queryRole = getQueryRoleFor(signature).id
        // updateRole = getUpdateRoleFor(signature).id

        worker = safeAdapter.firstImpl<AvValueWorker>()
    }

    override suspend fun process(operation: AvValueOperation) {
        // publicAccess() // ensureHas(updateRole)

        worker.queue(operation)
    }

    override suspend fun subscribe(conditions: List<AvSubscribeCondition>): AvValueSubscriptionId {
        // publicAccess() // ensureHas(queryRole)

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
        // publicAccess() // ensureHas(queryRole)

        worker.unsubscribe(subscriptionId)
    }

}