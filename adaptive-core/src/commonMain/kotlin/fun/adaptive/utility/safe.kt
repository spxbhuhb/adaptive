package `fun`.adaptive.utility

import `fun`.adaptive.log.AdaptiveLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

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
fun safeCall(logger: AdaptiveLogger, block: () -> Unit) {
    try {
        block()
    } catch (ex: Exception) {
        try {
            logger.error(ex)
        } catch (lex: Exception) {
            ex.printStackTrace()
            lex.printStackTrace()
        }
    }
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
suspend fun safeSuspendCall(logger: AdaptiveLogger, block: suspend () -> Unit) {
    try {
        block()
    } catch (ex: Exception) {
        try {
            logger.error(ex)
        } catch (lex: Exception) {
            ex.printStackTrace()
            lex.printStackTrace()
        }
    }
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
fun CoroutineScope.safeLaunch(logger: AdaptiveLogger, block: suspend () -> Unit) {
    launch {
        try {
            block()
        } catch (ex: Exception) {

            ensureActive()

            try {
                logger.error(ex)
            } catch (lex: Exception) {
                ex.printStackTrace()
                lex.printStackTrace()
            }
        }
    }
}