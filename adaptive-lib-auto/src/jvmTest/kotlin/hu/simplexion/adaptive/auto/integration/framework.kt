package hu.simplexion.adaptive.auto.integration

import hu.simplexion.adaptive.adat.toArray
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.BackendContext
import hu.simplexion.adaptive.auto.backend.PropertyBackend
import hu.simplexion.adaptive.auto.backend.TestData
import hu.simplexion.adaptive.auto.frontend.AdatClassFrontend
import hu.simplexion.adaptive.auto.model.AutoConnectInfo
import hu.simplexion.adaptive.auto.model.AutoHandle
import hu.simplexion.adaptive.auto.service.AutoService
import hu.simplexion.adaptive.auto.worker.AutoWorker
import hu.simplexion.adaptive.exposed.inMemoryH2
import hu.simplexion.adaptive.ktor.ktor
import hu.simplexion.adaptive.ktor.withProtoWebSocketTransport
import hu.simplexion.adaptive.lib.auth.auth
import hu.simplexion.adaptive.reflect.CallSiteName
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.server.builtin.service
import hu.simplexion.adaptive.server.builtin.worker
import hu.simplexion.adaptive.server.query.singleImpl
import hu.simplexion.adaptive.server.server
import hu.simplexion.adaptive.service.ServiceApi
import hu.simplexion.adaptive.service.defaultServiceImplFactory
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatProvider
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
            true,
            LamportTimestamp(1, 1)
        )

        val originBackend = PropertyBackend(
            context,
            LamportTimestamp(1, 1),
            TestData.adatWireFormat.propertyWireFormats,
            TestData(12, "a").toArray()
        )

        val originFrontend = AdatClassFrontend(
            originBackend,
            TestData,
            TestData(12, "a")
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
    test: suspend (originAdapter: AdaptiveServerAdapter, connectingAdapter: AdaptiveServerAdapter) -> Unit
) {
    val originAdapter = server {
        inMemoryH2(callSiteName.substringAfterLast('.'))
        worker { AutoWorker() }
        service { AutoTestService() }
        service { AutoService() }
        auth()
        ktor()
    }

    val connectingAdapter = server {
        worker { AutoWorker() }
        service { AutoService() }
    }

    runBlocking {
        val transport = withProtoWebSocketTransport("ws://localhost:8080/adaptive/service-ws", "http://localhost:8080/adaptive/client-id", false, connectingAdapter)

        defaultServiceImplFactory += connectingAdapter.singleImpl<AutoService>()

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
