package `fun`.adaptive.iot.infrastructure

import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auth.model.RoleId
import `fun`.adaptive.auth.util.getQueryRoleFor
import `fun`.adaptive.auth.util.getUpdateRoleFor
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.infrastructure.device.AioDeviceWorker
import `fun`.adaptive.iot.infrastructure.network.AioNetworkWorker
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.trimSignature

class AioInfrastructureService : ServiceImpl<AioInfrastructureService>, AioInfrastructureApi {

    companion object {
        val signature = AioInfrastructureService.typeSignature().trimSignature()

        lateinit var queryRole: RoleId
        lateinit var updateRole: RoleId

        lateinit var deviceWorker: AioDeviceWorker
        lateinit var networkWorker: AioNetworkWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)

        queryRole = getQueryRoleFor(signature).id
        updateRole = getUpdateRoleFor(signature).id

        deviceWorker = safeAdapter.firstImpl<AioDeviceWorker>()
        networkWorker = safeAdapter.firstImpl<AioNetworkWorker>()
    }

    override suspend fun query(): List<AioInfrastructureItem> {
        publicAccess()

        return networkWorker.query(emptySet()).map { it.toInfrastructureItem() } +
            deviceWorker.query(emptySet()).map { it.toInfrastructureItem() }
    }

}