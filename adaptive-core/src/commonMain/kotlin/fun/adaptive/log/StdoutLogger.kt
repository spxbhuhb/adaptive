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

    private fun log(level : String, message: String?, exception: Exception?) {
        println("$level: $prefix ${message ?: ""} ${exception?.stackTraceToString() ?: ""}".replace("%", "%%"))
    }

    override fun rawFine(message: String?, exception: Exception?) {
        log("FINE", message, exception)
    }

    override fun rawInfo(message: String?, exception: Exception?) {
        log("INFO", message, exception)
    }

    override fun rawWarning(message: String?, exception: Exception?) {
        log("WARNING", message, exception)
    }

    override fun rawError(message: String?, exception: Exception?) {
        log("ERROR", message, exception)
    }

    override fun fatal(message: String, exception: Exception?) : Nothing {
        log("FATAL", message, exception)
        exitProcessCommon(3210)
    }

}