package `fun`.adaptive.auto.integration

import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AutoTestApi {
    suspend fun testInstanceManual(): AutoConnectInfo
    suspend fun testInstanceWithOrigin(): AutoConnectInfo
    suspend fun testInstanceWithFile(): AutoConnectInfo
    suspend fun testListWithOrigin(): AutoConnectInfo
    suspend fun testPolyListWithOrigin(): AutoConnectInfo
}