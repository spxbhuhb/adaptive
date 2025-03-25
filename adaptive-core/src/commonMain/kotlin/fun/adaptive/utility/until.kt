package `fun`.adaptive.utility

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.datetime.Clock.System.now
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * Call [block] until the return value is not null or the [waitLimit]
 * has been reached.
 *
 * @param  catchTimeout  When true [TimeoutCancellationException] thrown by [block] is silently
 *                       discarded.
 * @param  waitStrategy  The delay strategy to use. Default is [Flat1SecStrategy]
 *                       (0.2 seconds initial delay, 1 second after that).
 */
suspend inline fun <reified T> untilNotNull(
    waitLimit: Duration,
    catchTimeout: Boolean = true,
    waitStrategy: WaitStrategy = Flat1SecStrategy,
    crossinline block: suspend () -> T?,
): T? {

    var value: T? = block()
    if (value != null) return value

    val cutoff = now() + waitLimit

    while (now() < cutoff) {
        coroutineContext.ensureActive()

        try {
            value = block()
            if (value != null) return value
        } catch (ex: TimeoutCancellationException) {
            if (! catchTimeout) throw ex
        }

        waitStrategy.wait()
    }

    return null
}

suspend inline fun <reified T> untilNotNull(
    catchTimeout: Boolean = true,
    waitStrategy: WaitStrategy = WaitStrategy(),
    crossinline block: suspend () -> T?,
): T {

    var value: T? = block()
    if (value != null) return value

    while (true) {
        coroutineContext.ensureActive()

        try {
            value = block()
            if (value != null) return value
        } catch (ex: TimeoutCancellationException) {
            if (! catchTimeout) throw ex
        }

        waitStrategy.wait()
    }
}

/**
 * Call [block] repeatedly until it does not throw an exception.
 *
 * - The default [waitStrategy] starts with 200 milliseconds delay and increments it by 15% up until 5 seconds.
 * - **All** exceptions are caught and hidden.
 * - Coroutine context activity is checked before each try with [ensureActive].
 */
suspend inline fun <reified T> untilSuccess(
    waitStrategy: WaitStrategy = WaitStrategy(),
    crossinline block: suspend () -> T,
): T {

    while (true) {
        coroutineContext.ensureActive()

        try {
            return block()
        } catch (_: Exception) {
            // this is intentional, feels a bit strange but the whole point is to try until success
        }

        waitStrategy.wait()
    }

}

suspend inline fun <reified T> untilNoTimeout(
    waitStrategy: WaitStrategy = WaitStrategy(),
    crossinline block: suspend () -> T,
): T {

    while (true) {
        coroutineContext.ensureActive()

        try {
            return block()
        } catch (_: TimeoutCancellationException) {
            // timeout is fine, others we can actually throw
        }

        waitStrategy.wait()
    }

}

/**
 * - first delay is 200 milliseconds
 * - each subsequent delay is 115% of the previous one
 * - maximum delay is 5 seconds (once reached, stays 5)
 */
object DefaultWaitStrategy : WaitStrategy()

/**
 * - each delay is 1 second
 */
object Flat1SecStrategy : WaitStrategy(100, 1.seconds, 1.seconds)

open class WaitStrategy(
    val retryMultiplier: Int = 115,
    val initialDelay: Duration = 200.milliseconds,
    maximumDelay: Duration = 5.seconds,
) {

    var retryDelay = initialDelay.inWholeMilliseconds
    val maximumDelayMillis = maximumDelay.inWholeMilliseconds

    suspend fun wait() {
        delay(retryDelay)
        if (retryDelay < maximumDelayMillis) retryDelay = (retryDelay * retryMultiplier) / 100
    }

    fun reset() {
        retryDelay = initialDelay.inWholeMilliseconds
    }
}