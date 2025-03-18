package `fun`.adaptive.iot.network

import `fun`.adaptive.auth.context.ensureHas
import `fun`.adaptive.auth.model.RoleId
import `fun`.adaptive.auth.util.getQueryRoleFor
import `fun`.adaptive.auth.util.getUpdateRoleFor
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.common.AioMarkerSet
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.runtime.GlobalRuntimeContext

class AioNetworkService : ServiceImpl<AioNetworkService>, AioNetworkApi {

    companion object {
        val signature = AioNetworkService.typeSignature()

        lateinit var queryRole: RoleId
        lateinit var updateRole: RoleId

        lateinit var worker: AioNetworkWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)

        queryRole = getQueryRoleFor(signature).id
        updateRole = getUpdateRoleFor(signature).id

        worker = adapter !!.firstImpl<AioNetworkWorker>()
    }


    override suspend fun query(markers: AioMarkerSet): List<AioNetwork> {
        ensureHas(queryRole)
        return worker.query(markers)
    }

}