package `fun`.adaptive.auto.integration

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.lib.auth.auth
import `fun`.adaptive.reflect.CallSiteName
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

@CallSiteName
fun autoTest(
    callSiteName: String = "unknown",
    port : Int,
    trace: Boolean = false,
    test: suspend (originAdapter: BackendAdapter, connectingAdapter: BackendAdapter) -> Unit
) {
    val originAdapter = backend {
        if (trace) it.trace = arrayOf(Regex(".*"))

        inMemoryH2(callSiteName.substringAfterLast('.'))

        ktor(port)
        auth()

        auto()

        service { AutoTestService() }
    }.start()

    val connectingAdapter = backend(webSocketTransport("http://localhost:$port")) {
        if (trace) it.trace = arrayOf(Regex(".*"))
        auto()
    }.start()

    runBlocking {
        try {
            test(originAdapter, connectingAdapter)
        } finally {
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
