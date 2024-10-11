package `fun`.adaptive.auto.testing

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.api.autoFile
import `fun`.adaptive.auto.api.autoFolder
import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.auto.api.autoList
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.utility.testPath
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlin.getValue

class AutoTestServiceInContext : AutoTestApi, ServiceImpl<AutoTestServiceInContext> {

    val worker by worker<AutoWorker>()

    override suspend fun instance(): AutoConnectionInfo<TestData> {
        val origin = autoInstance(worker, TestData, TestData(12, "a"), serviceContext = serviceContext)
        return origin.connectInfo()
    }

    override suspend fun list(): AutoConnectionInfo<List<TestData>> {
        return autoList(worker, TestData, serviceContext = serviceContext).connectInfo()
    }

    override suspend fun item(i: Int): AutoConnectionInfo<TestData> {
        throw UnsupportedOperationException()
    }

    override suspend fun polyList(): AutoConnectionInfo<List<AdatClass>> {
        return autoList<AdatClass>(worker, serviceContext = serviceContext).connectInfo()
    }

    override suspend fun file(): AutoConnectionInfo<TestData> {
        val path = Path(testPath, "AutoTestService.testInstanceWithFile.json")
        SystemFileSystem.delete(path, mustExist = false)
        return autoFile(worker, TestData, path, TestData(12, "a"), serviceContext = serviceContext).connectInfo()
    }

    override suspend fun folder(folderName: String): AutoConnectionInfo<List<TestData>> {

        val path = Path(testPath, folderName)

        return autoFolder<TestData>(
            worker,
            path,
            { itemId, _ -> "${itemId.peerId}.${itemId.timestamp}.json" },
            defaultWireFormat = TestData.adatWireFormat,
            serviceContext = serviceContext
        ).connectInfo()

    }

}