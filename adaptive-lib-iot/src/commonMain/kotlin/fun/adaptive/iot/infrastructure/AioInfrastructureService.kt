package `fun`.adaptive.iot.infrastructure

import `fun`.adaptive.auth.model.RoleId
import `fun`.adaptive.auth.util.getQueryRoleFor
import `fun`.adaptive.auth.util.getUpdateRoleFor
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.device.AioDeviceWorker
import `fun`.adaptive.iot.infrastructure.model.AioInfrastructureItem
import `fun`.adaptive.iot.network.AioNetworkWorker
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
        // TODO cache queries
        val networks = networkWorker.query(emptySet()).map { it.toInfrastructureItem() }
        val devices = deviceWorker.query(emptySet())
    }

}