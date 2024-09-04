package `fun`.adaptive.auto.integration

import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.api.originFile
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
import `fun`.adaptive.utility.ensureTestPath
import `fun`.adaptive.utility.testPath
import `fun`.adaptive.wireformat.json.JsonWireFormatProvider
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatProvider
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlin.getValue
import kotlin.test.BeforeTest


class AutoTestService : AutoTestApi, ServiceImpl<AutoTestService> {

    @BeforeTest
    fun setup() {
        ensureTestPath()
    }

    val worker by worker<AutoWorker>()

    override suspend fun testInstanceManual(): AutoConnectInfo {
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

    override suspend fun testInstanceWithOrigin(): AutoConnectInfo {
        return originInstance(worker, TestData(12, "a"), true).connectInfo()
    }

    override suspend fun testListWithOrigin(): AutoConnectInfo {
        return originList(worker, TestData, true).connectInfo()
    }

    override suspend fun testInstanceWithFile(): AutoConnectInfo {
        val path = Path(testPath, "AutoTestService.testInstanceWithFile.json")
        SystemFileSystem.delete(path, mustExist = false)
        return originFile(worker, TestData, path, JsonWireFormatProvider(), TestData(12, "a"), true).connectInfo()
    }

    override suspend fun testPolyListWithOrigin(): AutoConnectInfo {
        return originListPoly(worker, TestData, true).connectInfo()
    }

}