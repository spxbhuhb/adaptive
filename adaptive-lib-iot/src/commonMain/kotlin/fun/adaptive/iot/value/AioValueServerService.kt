package `fun`.adaptive.iot.value

import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auth.model.RoleId
import `fun`.adaptive.auth.util.getQueryRoleFor
import `fun`.adaptive.auth.util.getUpdateRoleFor
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.item.AioMarker
import `fun`.adaptive.iot.value.operation.AioValueOperation
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.trimSignature

class AioValueServerService : ServiceImpl<AioValueServerService>, AioValueApi {

    companion object {
        val signature = AioValueServerService.typeSignature().trimSignature()

        lateinit var queryRole: RoleId
        lateinit var updateRole: RoleId

        lateinit var worker: AioValueWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)

        queryRole = getQueryRoleFor(signature).id
        updateRole = getUpdateRoleFor(signature).id

        worker = safeAdapter.firstImpl<AioValueWorker>()
    }

    override suspend fun process(operation: AioValueOperation) {
        publicAccess() // ensureHas(updateRole)

        worker.process(operation)
    }

    override suspend fun subscribe(valueIds: List<AioValueId>, markerIds : List<AioMarker>): AuiValueSubscriptionId {
        publicAccess() // ensureHas(queryRole)

        val subscription = AioValueClientSubscription(
            uuid4(),
            valueIds,
            markerIds,
            serviceContext.transport,
            safeAdapter.scope
        )

        worker.subscribe(subscription)

        return subscription.uuid
    }

    override suspend fun unsubscribe(subscriptionId: AuiValueSubscriptionId) {
        publicAccess() // ensureHas(queryRole)

        worker.unsubscribe(subscriptionId)
    }

}