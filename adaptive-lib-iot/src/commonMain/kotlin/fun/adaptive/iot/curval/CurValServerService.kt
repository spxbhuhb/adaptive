package `fun`.adaptive.iot.curval

import `fun`.adaptive.auth.context.ensureHas
import `fun`.adaptive.auth.model.RoleId
import `fun`.adaptive.auth.util.getQueryRoleFor
import `fun`.adaptive.auth.util.getUpdateRoleFor
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.UUID.Companion.uuid4

class CurValServerService : ServiceImpl<CurValServerService>, CurValApi {

    companion object {
        val signature = CurValServerService.typeSignature()

        lateinit var queryRole: RoleId
        lateinit var updateRole: RoleId

        lateinit var worker: CurValWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)

        queryRole = getQueryRoleFor(signature).id
        updateRole = getUpdateRoleFor(signature).id

        worker = adapter !!.firstImpl<CurValWorker>()
    }

    override suspend fun update(curVal: CurVal) {
        ensureHas(updateRole)

        worker.update(curVal)
    }

    override suspend fun subscribe(valueIds: List<AioValueId>) : CurValSubscriptionId {
        ensureHas(queryRole)

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
        ensureHas(queryRole)

        worker.unsubscribe(subscriptionId)
    }

}