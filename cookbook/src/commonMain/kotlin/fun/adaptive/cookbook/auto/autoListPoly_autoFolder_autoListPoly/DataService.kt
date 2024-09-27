package `fun`.adaptive.cookbook.auto.autoListPoly_autoFolder_autoListPoly

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker

class DataService : DataServiceApi, ServiceImpl<DataService> {

    val dataWorker by worker<MasterDataWorker>()

    override suspend fun getConnectInfo(): AutoConnectionInfo<List<AdatClass>> {
        publicAccess()
        return dataWorker.connectInfo()
    }

}