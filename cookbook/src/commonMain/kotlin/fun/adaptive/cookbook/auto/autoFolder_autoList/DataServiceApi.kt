package `fun`.adaptive.cookbook.auto.autoFolder_autoList

import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface DataServiceApi {

    suspend fun getConnectInfo(): AutoConnectInfo<List<MasterDataItem>>

}