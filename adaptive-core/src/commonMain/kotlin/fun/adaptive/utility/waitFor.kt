package `fun`.adaptive.utility

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
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

suspend inline fun <reified T> untilNotNull(
    catchTimeout: Boolean = true,
    waitStrategy: WaitStrategy = WaitStrategy(),
    crossinline block: suspend () -> T?
): T {

    var value: T? = block()
    if (value != null) return value

    while (true) {
        try {
            value = block()
            if (value != null) return value
        } catch (ex: TimeoutCancellationException) {
            if (! catchTimeout) throw ex
        }

        waitStrategy.wait()
    }

}

suspend inline fun <reified T> untilNoTimeout(
    waitStrategy: WaitStrategy = WaitStrategy(),
    crossinline block: suspend () -> T
): T {

    while (true) {

        try {
            return block()
        } catch (_: TimeoutCancellationException) {
            // timeout is fine, others we can actually throw
        }

        waitStrategy.wait()
    }

}

class WaitStrategy(
    val retryMultiplier : Int = 115,
    initialDelay : Duration = 200.milliseconds,
    maximumDelay : Duration = 5.seconds
) {

    var retryDelay = initialDelay.inWholeMilliseconds
    val maximumDelayMillis = maximumDelay.inWholeMilliseconds

    suspend fun wait() {
        delay(retryDelay)
        if (retryDelay < maximumDelayMillis) retryDelay = (retryDelay * retryMultiplier) / 100
    }
}