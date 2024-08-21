package `fun`.adaptive.auto.integration

import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.internal.backend.BackendContext
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.service.AutoService
import `fun`.adaptive.auto.worker.AutoWorker
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.ktor.withProtoWebSocketTransport
import `fun`.adaptive.lib.auth.auth
import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.query.singleImpl
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.service.defaultServiceImplFactory
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout


@ServiceApi
interface AutoTestApi {
    suspend fun testInstance(): AutoConnectInfo
}

class AutoTestService : AutoTestApi, ServiceImpl<AutoTestService> {

    val worker by worker<AutoWorker>()

    override suspend fun testInstance(): AutoConnectInfo {
        val context = BackendContext(
            AutoHandle(UUID(), 1),
            worker.scope,
            ProtoWireFormatProvider(),
            TestData.adatMetadata,
            TestData.adatWireFormat,
            true,
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
            TestData,
            TestData(12, "a"),
            null
        )

        originBackend.frontEnd = originFrontend

        worker.register(originBackend)

        return AutoConnectInfo(
            context.handle,
            context.time,
            AutoHandle(context.handle.globalId, 2),
        )
    }

}

@CallSiteName
fun autoTest(
    callSiteName: String = "unknown",
    port : Int,
    test: suspend (originAdapter: BackendAdapter, connectingAdapter: BackendAdapter) -> Unit
) {
    val originAdapter = backend {
        settings {
            inline("KTOR_PORT" to port)
        }
        inMemoryH2(callSiteName.substringAfterLast('.'))
        worker { AutoWorker() }
        service { AutoTestService() }
        service { AutoService() }
        auth()
        ktor()
    }

    val connectingAdapter = backend {
        worker { AutoWorker() }
        service { AutoService() }
    }

    runBlocking {
        val transport = withProtoWebSocketTransport("http://localhost:$port", serviceImplFactory = connectingAdapter)

        try {
            test(originAdapter, connectingAdapter)
        } finally {
            transport.stop()
            originAdapter.stop()
            connectingAdapter.stop()
        }
    }
}

suspend fun waitForSync(w1: AutoWorker, h1: AutoHandle, w2: AutoWorker, h2: AutoHandle) {
    withTimeout(1000) {
        while (w1.peerTime(h1).timestamp != w2.peerTime(h2).timestamp) {
            delay(10)
        }
    }
}
