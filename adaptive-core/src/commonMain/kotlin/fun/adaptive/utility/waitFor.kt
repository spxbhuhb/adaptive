package `fun`.adaptive.utility

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration

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