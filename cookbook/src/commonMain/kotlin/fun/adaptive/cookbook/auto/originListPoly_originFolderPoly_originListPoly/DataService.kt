package `fun`.adaptive.cookbook.auto.originListPoly_originFolderPoly_originListPoly

import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker

class DataService : DataServiceApi, ServiceImpl<DataService> {

    val dataWorker by worker<MasterDataWorker>()

    override suspend fun getConnectInfo(): AutoConnectInfo {
        publicAccess()
        return dataWorker.connectInfo()
    }

}