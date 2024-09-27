package `fun`.adaptive.cookbook.auto.autoFolder_autoList

import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface DataServiceApi {

    suspend fun getConnectInfo(): AutoConnectionInfo<List<MasterDataItem>>

}