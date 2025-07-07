/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.log

import `fun`.adaptive.utility.exitProcessCommon

class BrowserConsoleLogger(
    name: String
) : AdaptiveLogger() {

    val prefix = "[$name]"

    override fun rawFine(message: String?, throwable: Throwable?) {
        if (usePrintln) {
            println("FINE: [$prefix] $message ${throwable?.stackTraceToString() ?: ""}")
        } else {
            console.info(prefix, message, throwable)
        }
    }

    override fun rawInfo(message: String?, throwable: Throwable?) {
        if (usePrintln) {
            println("INFO: [$prefix] $message ${throwable?.stackTraceToString() ?: ""}")
        } else {
            console.info(prefix, message, throwable)
        }
    }

    override fun rawWarning(message: String?, throwable: Throwable?) {
        console.error(prefix, message, throwable)
    }

    override fun rawError(message: String?, throwable: Throwable?) {
        console.error(prefix, message, throwable)
    }

    override fun fatal(message: String, throwable: Throwable?): Nothing {
        println("FATAL: [$prefix] $message ${throwable?.stackTraceToString()}")
        exitProcessCommon(3210)
    }

}