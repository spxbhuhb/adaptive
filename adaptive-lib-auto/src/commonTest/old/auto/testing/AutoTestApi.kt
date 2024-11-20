package `fun`.adaptive.auto.testing

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AutoTestApi {
    suspend fun instance(): AutoConnectionInfo<TestData>

    suspend fun list(): AutoConnectionInfo<List<TestData>>
    suspend fun item(i: Int): AutoConnectionInfo<TestData>?

    suspend fun polyList(): AutoConnectionInfo<List<AdatClass>>

    suspend fun file(): AutoConnectionInfo<TestData>
    suspend fun folder(folderName: String): AutoConnectionInfo<List<TestData>>
}