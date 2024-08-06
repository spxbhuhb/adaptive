package hu.simplexion.adaptive.auto.integration

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.AutoInstance
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

@Adat
class TestData(
    val i: Int,
    val s: String
) : AdatClass<TestData> {
    companion object : AdatCompanion<TestData>
}

@ServiceApi
interface AutoTestApi {
    suspend fun testInstance(): AutoConnectInfo
}

class AutoTestService : AutoTestApi, ServiceImpl<AutoTestService> {

    val worker by worker<AutoWorker>()

    override suspend fun testInstance(): AutoConnectInfo {
        val instance = AutoInstance(
            UUID(),
            worker.scope,
            LamportTimestamp(1, 1),
            TestData,
            TestData(12, "a"),
            ProtoWireFormatProvider()
        )

        worker.register(instance)

        return AutoConnectInfo(
            instance.handle,
            instance.time,
            AutoHandle(instance.globalId, 2),
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
