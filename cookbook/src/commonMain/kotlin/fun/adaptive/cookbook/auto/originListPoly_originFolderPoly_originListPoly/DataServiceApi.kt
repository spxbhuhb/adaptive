package `fun`.adaptive.cookbook.auto.originListPoly_originFolderPoly_originListPoly

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.cookbook.auto.originFolder_originList.MasterDataItem
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface DataServiceApi {

    suspend fun getConnectInfo(): AutoConnectInfo<List<AdatClass>>

}