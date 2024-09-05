package `fun`.adaptive.auto.integration

import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.api.originFile
import `fun`.adaptive.auto.api.originFolder
import `fun`.adaptive.auto.api.originInstance
import `fun`.adaptive.auto.api.originList
import `fun`.adaptive.auto.api.originListPoly
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
import `fun`.adaptive.wireformat.json.JsonWireFormatProvider
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatProvider
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlin.getValue

class AutoTestService : AutoTestApi, ServiceImpl<AutoTestService> {

    val worker by worker<AutoWorker>()

    override suspend fun manual(): AutoConnectInfo {
        val logger = getLogger("logger")

        val context = BackendContext(
            AutoHandle(UUID(), 1),
            worker.scope,
            logger,
            ProtoWireFormatProvider(),
            TestData.adatMetadata,
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

        return AutoConnectInfo(
            context.handle,
            context.time,
            AutoHandle(context.handle.globalId, 2),
        )
    }

    override suspend fun instance(): AutoConnectInfo {
        val origin = originInstance(worker, TestData(12, "a"), serviceContext)
        return origin.connectInfo()
    }

    override suspend fun list(): AutoConnectInfo {
        return originList(worker, TestData, serviceContext).connectInfo()
    }

    override suspend fun polyList(): AutoConnectInfo {
        return originListPoly(worker, TestData, serviceContext).connectInfo()
    }

    override suspend fun file(): AutoConnectInfo {
        val path = Path(testPath, "AutoTestService.testInstanceWithFile.json")
        SystemFileSystem.delete(path, mustExist = false)
        return originFile(worker, TestData, path, TestData(12, "a"), JsonWireFormatProvider(), serviceContext).connectInfo()
    }

    override suspend fun folder(folderName : String): AutoConnectInfo {

        val path = Path(testPath, folderName)

        return originFolder(
            worker,
            TestData,
            JsonWireFormatProvider(),
            path,
            { itemId,_ -> "${itemId.clientId}.${itemId.timestamp}.json" },
            serviceContext
        ).connectInfo()

    }

}