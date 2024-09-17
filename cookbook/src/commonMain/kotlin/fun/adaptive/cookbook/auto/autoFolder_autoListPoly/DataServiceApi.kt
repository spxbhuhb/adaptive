package `fun`.adaptive.cookbook.auto.autoFolder_autoListPoly

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface DataServiceApi {

    suspend fun getConnectInfo(): AutoConnectInfo<List<AdatClass>>

}