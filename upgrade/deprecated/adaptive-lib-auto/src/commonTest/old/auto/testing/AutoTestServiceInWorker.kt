package `fun`.adaptive.auto.testing

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker
import kotlin.getValue

class AutoTestServiceInWorker : AutoTestApi, ServiceImpl<AutoTestServiceInWorker> {

    val testWorker by worker<AutoTestWorker>()

    override suspend fun instance(): AutoConnectionInfo<TestData> {
        return testWorker.instance()
    }

    override suspend fun list(): AutoConnectionInfo<List<TestData>> {
        return testWorker.list()
    }

    override suspend fun item(i: Int): AutoConnectionInfo<TestData>? {
        return testWorker.item(i)
    }

    override suspend fun polyList(): AutoConnectionInfo<List<AdatClass>> {
        return testWorker.polyList()
    }

    override suspend fun file(): AutoConnectionInfo<TestData> {
        TODO("Not yet implemented")
    }

    override suspend fun folder(folderName: String): AutoConnectionInfo<List<TestData>> {
        TODO("Not yet implemented")
    }


}