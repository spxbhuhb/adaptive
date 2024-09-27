package `fun`.adaptive.cookbook.auto.autoFolder_autoFile

import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface DataServiceApi {

    suspend fun getConnectInfo(filter: String): AutoConnectionInfo<DataItem>

}