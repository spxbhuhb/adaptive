package `fun`.adaptive.auto.integration

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AutoTestApi {
    suspend fun manual(): AutoConnectInfo<TestData>

    suspend fun instance(): AutoConnectInfo<TestData>
    suspend fun list(): AutoConnectInfo<List<TestData>>
    suspend fun polyList(): AutoConnectInfo<List<AdatClass>>

    suspend fun file(): AutoConnectInfo<TestData>
    suspend fun folder(folderName : String) : AutoConnectInfo<List<TestData>>
}