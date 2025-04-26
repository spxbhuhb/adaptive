/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.log

import `fun`.adaptive.utility.exitProcessCommon

class BrowserConsoleLogger(
    name: String
) : AdaptiveLogger() {

    val prefix = "[$name]"

    override fun rawFine(message: String?, exception: Exception?) {
        if (usePrintln) {
            println("FINE: [$prefix] $message ${exception?.stackTraceToString() ?: ""}")
        } else {
            console.info(prefix, message, exception)
        }
    }

    override fun rawInfo(message: String?, exception: Exception?) {
        if (usePrintln) {
            println("INFO: [$prefix] $message ${exception?.stackTraceToString() ?: ""}")
        } else {
            console.info(prefix, message, exception)
        }
    }

    override fun rawWarning(message: String?, exception: Exception?) {
        console.error(prefix, message, exception)
    }

    override fun rawError(message: String?, exception: Exception?) {
        console.error(prefix, message, exception)
    }

    override fun fatal(message: String, exception: Exception?): Nothing {
        println("FATAL: [$prefix] $message ${exception?.stackTraceToString()}")
        exitProcessCommon(3210)
    }

}