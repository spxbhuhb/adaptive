package `fun`.adaptive.auto.integration

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.api.autoFile
import `fun`.adaptive.auto.api.autoFolder
import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.auto.api.autoList
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.internal.backend.BackendContext
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.testPath
import `fun`.adaptive.wireformat.api.Proto
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlin.getValue

class AutoTestService : AutoTestApi, ServiceImpl<AutoTestService> {

    val worker by worker<AutoWorker>()

    override suspend fun manual(): AutoConnectionInfo<TestData> {
        val logger = getLogger("logger")

        val context = BackendContext<TestData>(
            AutoHandle(UUID(), 0, null),
            worker.scope,
            logger,
            Proto,
            TestData.adatWireFormat,
            LamportTimestamp.ORIGIN,
        )

        val originBackend = PropertyBackend(
            context,
            LamportTimestamp.ORIGIN,
            null,
            TestData(12, "a").toArray()
        )

        val originFrontend = AdatClassFrontend(
            originBackend,
            TestData.adatWireFormat,
            TestData(12, "a"),
            LamportTimestamp.ORIGIN,
            null
        )

        originBackend.frontend = originFrontend

        worker.register(originBackend)

        return AutoConnectionInfo<TestData>(
            AutoConnectionType.Service,
            context.handle,
            context.time,
            AutoHandle(context.handle.globalId, 2, null)
        )
    }

    override suspend fun item(): AutoConnectionInfo<TestData> {
        val origin = autoItem(worker, TestData, TestData(12, "a"), serviceContext = serviceContext)
        return origin.connectInfo()
    }

    override suspend fun list(): AutoConnectionInfo<List<TestData>> {
        return autoList(worker, TestData, serviceContext = serviceContext).connectInfo()
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