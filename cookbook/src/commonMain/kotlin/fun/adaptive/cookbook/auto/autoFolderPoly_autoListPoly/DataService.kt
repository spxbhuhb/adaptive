package `fun`.adaptive.cookbook.auto.autoFolderPoly_autoListPoly

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker

class DataService : DataServiceApi, ServiceImpl<DataService> {

    val dataWorker by worker<MasterDataWorker>()

    override suspend fun getConnectInfo(): AutoConnectInfo<List<AdatClass>> {
        publicAccess()
        return dataWorker.connectInfo()
    }

}