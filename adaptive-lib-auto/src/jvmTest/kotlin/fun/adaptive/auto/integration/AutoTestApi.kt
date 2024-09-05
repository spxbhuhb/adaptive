package `fun`.adaptive.auto.integration

import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AutoTestApi {
    suspend fun manual(): AutoConnectInfo

    suspend fun instance(): AutoConnectInfo
    suspend fun list(): AutoConnectInfo
    suspend fun polyList(): AutoConnectInfo

    suspend fun file(): AutoConnectInfo
    suspend fun folder(folderName : String) : AutoConnectInfo
}