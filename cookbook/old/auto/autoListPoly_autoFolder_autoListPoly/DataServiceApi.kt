import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface DataServiceApi {

    suspend fun getConnectInfo(): AutoConnectionInfo<List<AdatClass>>

}