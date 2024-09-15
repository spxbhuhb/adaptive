package `fun`.adaptive.cookbook.auto.originListPoly_originFolderPoly_originListPoly

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.cookbook.auto.originFolder_originList.MasterDataItem

class DataService : DataServiceApi, ServiceImpl<DataService> {

    val dataWorker by worker<MasterDataWorker>()

    override suspend fun getConnectInfo(): AutoConnectInfo<List<AdatClass>> {
        publicAccess()
        return dataWorker.connectInfo()
    }

}