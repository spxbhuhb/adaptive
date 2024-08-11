package hu.simplexion.adaptive.utility

import hu.simplexion.adaptive.log.AdaptiveLogger

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