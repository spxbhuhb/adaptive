package `fun`.adaptive.cookbook.auto.autoFile_autoFile

import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface DataServiceApi {

    suspend fun getConnectInfo(): AutoConnectInfo<DataItem>

}