package `fun`.adaptive.auto.integration

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.backend.AutoService
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.ktor.withProtoWebSocketTransport
import `fun`.adaptive.lib.auth.auth
import `fun`.adaptive.reflect.CallSiteName
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

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

        ktor()
        auth()

        auto()

        service { AutoTestService() }
    }

    val connectingAdapter = backend {
        auto()
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
