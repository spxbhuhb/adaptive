package `fun`.adaptive.auto.integration

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.api.autoFile
import `fun`.adaptive.auto.api.autoFolder
import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.auto.api.autoList
import `fun`.adaptive.auto.api.autoListPoly
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.internal.backend.BackendContext
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.testPath
import `fun`.adaptive.wireformat.api.Json
import `fun`.adaptive.wireformat.api.Proto
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlin.getValue

class AutoTestService : AutoTestApi, ServiceImpl<AutoTestService> {

    val worker by worker<AutoWorker>()

    override suspend fun manual(): AutoConnectInfo<TestData> {
        val logger = getLogger("logger")

        val context = BackendContext(
            AutoHandle(UUID(), 1, null),
            worker.scope,
            logger,
            Proto,
            TestData.adatWireFormat,
            LamportTimestamp(1, 1)
        )

        val originBackend = PropertyBackend(
            context,
            LamportTimestamp(1, 1),
            null,
            TestData(12, "a").toArray()
        )

        val originFrontend = AdatClassFrontend(
            originBackend,
            TestData.adatWireFormat,
            TestData(12, "a"),
            null, null, null
        )

        originBackend.frontEnd = originFrontend

        worker.register(originBackend)

        return AutoConnectInfo<TestData>(
            context.handle,
            context.time,
            AutoHandle(context.handle.globalId, 2, null)
        )
    }

    override suspend fun instance(): AutoConnectInfo<TestData> {
        val origin = autoInstance(worker, TestData(12, "a"), serviceContext)
        return origin.connectInfo()
    }

    override suspend fun list(): AutoConnectInfo<List<TestData>> {
        return autoList(worker, TestData, serviceContext).connectInfo()
    }

    override suspend fun polyList(): AutoConnectInfo<List<AdatClass>> {
        return autoListPoly(worker, serviceContext = serviceContext).connectInfo()
    }

    override suspend fun file(): AutoConnectInfo<TestData> {
        val path = Path(testPath, "AutoTestService.testInstanceWithFile.json")
        SystemFileSystem.delete(path, mustExist = false)
        return autoFile(worker, TestData, path, TestData(12, "a"), Json, serviceContext).connectInfo()
    }

    override suspend fun folder(folderName : String): AutoConnectInfo<List<TestData>> {

        val path = Path(testPath, folderName)

        return autoFolder(
            worker,
            TestData,
            Json,
            path,
            { itemId,_ -> "${itemId.peerId}.${itemId.timestamp}.json" },
            serviceContext
        ).connectInfo()

    }

}