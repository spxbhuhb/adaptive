package `fun`.adaptive.utility

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.datetime.Clock.System.now
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

suspend fun waitFor(timeout: Duration, condition: () -> Boolean) {
    withTimeout(timeout) {
        while (! condition()) {
            delay(50)
        }
    }
}

suspend fun waitForSuspend(timeout: Duration, condition: suspend () -> Boolean) {
    withTimeout(timeout) {
        while (! condition()) {
            delay(50)
        }
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
suspend fun waitForReal(timeout: Duration, condition: () -> Boolean) {
    withContext(Dispatchers.Default.limitedParallelism(1)) {
        waitFor(timeout, condition)
    }
}

suspend inline fun <reified T> waitForNotNull(timeout: Duration, crossinline block: () -> T?): T =
    block() ?: withTimeout(timeout) {
        var value: T?
        do {
            value = block()
            if (value == null) {
                delay(50)
            }
        } while (value == null)
        value
    }