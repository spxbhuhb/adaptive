package `fun`.adaptive.cookbook.auto.originFolder_originFile

import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker

class DataService : DataServiceApi, ServiceImpl<DataService> {

    val dataWorker by worker<DataWorker>()

    override suspend fun getConnectInfo(filter : String): AutoConnectInfo<DataItem> {
        publicAccess()
        return dataWorker.connectInfo(filter)
    }

}