package `fun`.adaptive.cookbook.auto.originFolder_originFile

import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface DataServiceApi {

    suspend fun getConnectInfo(filter : String): AutoConnectInfo<DataItem>

}