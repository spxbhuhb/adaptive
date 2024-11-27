package `fun`.adaptive.utility

import `fun`.adaptive.log.AdaptiveLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

/**
 * Call [block] in a try/catch and catch **ALL** exceptions thrown. If an exception
 * is thrown, use [logger] to log it. If [logger] fails as well, use `printStackTrace`
 * to write out the exceptions.
 *
 * @param lock    When not null, [block] will run with this lock acquired.
 *
 * @param message The message to use in the log when there is an exception. Uses the
 *                `message` of the exception when null.
 * @param block   The block to run.
 *
 * @return  The return value of [block] or `null` if an exception has been thrown.
 */
fun <T> safeCall(logger: AdaptiveLogger, lock : Lock? = null, message : String? = null, block: () -> T) : T? =

    try {
        if (lock != null) {
            lock.use { block() }
        } else {
            block()
        }
    } catch (ex: Exception) {
        try {
            logger.error(message ?: ex.message ?: "", ex)
        } catch (lex: Exception) {
            ex.printStackTrace()
            lex.printStackTrace()
        }
        null
    }

/**
 * Call [block] in a try/catch and catch **ALL** exceptions thrown. If an exception
 * is thrown, use [logger] to log it. If [logger] fails as well, use `printStackTrace`
 * to write out the exceptions.
 *
 * This function is meant to be used in cleanup code where you don't want a call to
 * stop others even if it fails.
 *
 * **NOTE** Best is if your cleanup code does not throw exceptions, however, it may
 * be a tad bit hard to cover it all, hence [safeCall].
 */
suspend fun <T> safeSuspendCall(logger: AdaptiveLogger, message: String? = null, block: suspend () -> T): T? =
    try {
        block()
    } catch (ex: Exception) {
        try {
            logger.error(message ?: ex.message ?: "", ex)
        } catch (lex: Exception) {
            ex.printStackTrace()
            lex.printStackTrace()
        }
        null
    }

/**
 * Launch [block] in a try/catch and catch **ALL** exceptions thrown. If an exception
 * is thrown, use [logger] to log it. If [logger] fails as well, use `printStackTrace`
 * to write out the exceptions.
 *
 * This function is meant to be used in cleanup code where you don't want a call to
 * stop others even if it fails.
 *
 * **NOTE** Best is if your cleanup code does not throw exceptions, however, it may
 * be a tad bit hard to cover it all, hence [safeCall].
 */
fun CoroutineScope.safeLaunch(logger: AdaptiveLogger, message : String? = null, block: suspend () -> Unit) : Job =

    launch {
        try {
            block()
        } catch (ex: Exception) {

            ensureActive()

            try {
                logger.error(message ?: ex.message ?: "", ex)
            } catch (lex: Exception) {
                ex.printStackTrace()
                lex.printStackTrace()
            }
        }
    }