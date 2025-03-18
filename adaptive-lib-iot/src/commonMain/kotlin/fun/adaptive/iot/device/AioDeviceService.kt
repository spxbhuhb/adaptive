package `fun`.adaptive.iot.device

import `fun`.adaptive.auth.context.ensureHas
import `fun`.adaptive.auth.model.RoleId
import `fun`.adaptive.auth.util.getQueryRoleFor
import `fun`.adaptive.auth.util.getUpdateRoleFor
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.common.AioMarkerSet
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.trimSignature

class AioDeviceService : ServiceImpl<AioDeviceService>, AioDeviceApi {

    companion object {
        val signature = AioDeviceService.typeSignature().trimSignature()

        lateinit var queryRole: RoleId
        lateinit var updateRole: RoleId

        lateinit var worker: AioDeviceWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)

        queryRole = getQueryRoleFor(signature).id
        updateRole = getUpdateRoleFor(signature).id

        worker = adapter !!.firstImpl<AioDeviceWorker>()
    }


    override suspend fun query(markers: AioMarkerSet): List<AioDevice> {
        ensureHas(queryRole)
        return worker.query(markers)
    }

}