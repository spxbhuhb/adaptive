/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.log

import `fun`.adaptive.utility.exitProcessCommon

/**
 * Write out all logs to the standard output with [println].
 */
class StdoutLogger(
    name: String,
) : AdaptiveLogger() {

    val prefix = "[$name]"

    private fun log(level : String, message: String?, throwable: Throwable?) {
        println("$level: $prefix ${message ?: ""} ${throwable?.stackTraceToString() ?: ""}".replace("%", "%%"))
    }

    override fun rawFine(message: String?, throwable: Throwable?) {
        log("FINE", message, throwable)
    }

    override fun rawInfo(message: String?, throwable: Throwable?) {
        log("INFO", message, throwable)
    }

    override fun rawWarning(message: String?, throwable: Throwable?) {
        log("WARNING", message, throwable)
    }

    override fun rawError(message: String?, throwable: Throwable?) {
        log("ERROR", message, throwable)
    }

    override fun fatal(message: String, throwable: Throwable?) : Nothing {
        log("FATAL", message, throwable)
        exitProcessCommon(3210)
    }

}