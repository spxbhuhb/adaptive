package `fun`.adaptive.auto.integration.connector

import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker

class DataService : DataServiceApi, ServiceImpl<DataService> {

    val dataWorker by worker<DataWorker>()

    override suspend fun getConnectInfo(): AutoConnectionInfo<DataItem> {
        publicAccess()
        return dataWorker.connectInfo()
    }

}