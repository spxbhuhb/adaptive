package `fun`.adaptive.value

import `fun`.adaptive.log.getLogger
import `fun`.adaptive.value.embedded.EmbeddedValueServer
import `fun`.adaptive.value.embedded.EmbeddedValueServer.Companion.withEmbeddedValueServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun condition(valueId: AvValueId) = listOf(AvSubscribeCondition(valueId))
fun condition(marker: String) = listOf(AvSubscribeCondition(marker = marker))

@OptIn(ExperimentalCoroutinesApi::class)
fun valueTest(timeout: Duration = 10.seconds, testFun: suspend EmbeddedValueServer.() -> Unit) =
    runTest(timeout = timeout) {
        withEmbeddedValueServer(testFun)
    }


fun standaloneTest(timeout: Duration = 10.seconds, testFun: suspend (worker: AvValueWorker) -> Unit) =
    runTest(timeout = timeout) {
        val worker = AvValueWorker(proxy = false)
        worker.logger = getLogger("worker")
        val dispatcher = Dispatchers.Unconfined
        val scope = CoroutineScope(dispatcher)

        scope.launch {
            worker.mount()
            worker.run()
        }

        testFun(worker)
    }