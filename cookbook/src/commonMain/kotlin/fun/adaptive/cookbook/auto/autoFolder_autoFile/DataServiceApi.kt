package `fun`.adaptive.cookbook.auto.autoFolder_autoFile

import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface DataServiceApi {

    suspend fun getConnectInfo(filter : String): AutoConnectInfo<DataItem>

}