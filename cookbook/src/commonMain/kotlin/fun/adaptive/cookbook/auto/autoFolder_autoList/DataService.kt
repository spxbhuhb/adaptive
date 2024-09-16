package `fun`.adaptive.cookbook.auto.autoFolder_autoList

import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker

class DataService : DataServiceApi, ServiceImpl<DataService> {

    val dataWorker by worker<MasterDataWorker>()

    override suspend fun getConnectInfo(): AutoConnectInfo<List<MasterDataItem>> {
        publicAccess()
        return dataWorker.connectInfo()
    }

}