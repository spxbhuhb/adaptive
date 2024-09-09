package `fun`.adaptive.cookbook.auto.originListPoly_originFolderPoly_originListPoly

import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface DataServiceApi {

    suspend fun getConnectInfo(): AutoConnectInfo

}